package com.example.board.utils;

import com.example.board.common.exceptions.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static com.example.board.common.response.BaseResponseStatus.EMPTY_JWT;
import static com.example.board.common.response.BaseResponseStatus.INVALID_JWT;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;

    private Key generateKey() {
        byte[] secretKeyBytes = JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /*
   JWT 생성
   @param userId
   @return String
    */
    public String createJwt(Long userId){
        Date now = new Date();
        Key key = generateKey();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(key)
                .compact();
    }

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
    */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /*
    JWT에서 userId 추출
    @return Long
    @throws BaseException
     */

    public Long getUserId() throws BaseException {
        //1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("userId",Long.class);
    }
}
