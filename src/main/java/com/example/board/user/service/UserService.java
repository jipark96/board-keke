package com.example.board.user.service;


import com.example.board.common.exceptions.BaseException;
import com.example.board.user.dto.JoinRequestDto;
import com.example.board.user.dto.JoinResponseDto;
import com.example.board.user.entity.User;
import com.example.board.user.repository.UserRepository;
import com.example.board.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.board.common.entity.BaseEntity.State.ACTIVE;
import static com.example.board.common.response.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    //[회원 가입]
    public JoinResponseDto createUser(JoinRequestDto joinRequestDto) {
        //[아이디 중복 체크]
        Optional<User> checkUser = userRepository.findByUsernameAndState(joinRequestDto.getUsername(), ACTIVE);
        if (checkUser.isPresent() == true) {
            throw new BaseException(POST_USERS_EXISTS_USERNAME);
        }

        User saveUser = userRepository.save(joinRequestDto.toEntity());
        String jwtToken = jwtService.createJwt(saveUser.getId());

        return JoinResponseDto.builder()
                .id(saveUser.getId())
                .email(saveUser.getEmail())
                .username(saveUser.getUsername())
                .name(saveUser.getName())
                .jwtToken(jwtToken)
                .build();
    }

}
