package com.example.board.user.service;

import com.example.board.common.exceptions.BaseException;
import com.example.board.user.dto.*;
import com.example.board.user.entity.User;
import com.example.board.user.repository.UserRepository;
import com.example.board.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.board.common.entity.BaseEntity.State.ACTIVE;
import static com.example.board.common.response.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    //[회원 가입]
    public JoinResponseDto createUser(JoinRequestDto joinRequestDto) {
        //[아이디 중복 체크]
        Optional<User> checkUser = userRepository.findByUsernameAndState(joinRequestDto.getUsername(), ACTIVE);
        if (checkUser.isPresent() == true) {
            throw new BaseException(POST_USERS_EXISTS_USERNAME);
        }

        User saveUser = userRepository.save(joinRequestDto.toEntity(bCryptPasswordEncoder));
        String jwtToken = jwtService.createJwt(saveUser.getId());

        return JoinResponseDto.builder()
                .id(saveUser.getId())
                .email(saveUser.getEmail())
                .username(saveUser.getUsername())
                .name(saveUser.getName())
                .jwtToken(jwtToken)
                .build();
    }

    //[회원 조회]
    @Transactional(readOnly = true)
    public List<GetUserDto> getUsers() {
        List<GetUserDto> getUserDtoList = userRepository.findByState(ACTIVE).stream()
                .map(GetUserDto::new)
                .collect(Collectors.toList());
        return getUserDtoList;
    }

    //[로그인]
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsernameAndState(loginRequestDto.getUsername(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

//        if(!user.getPassword().equals(loginRequestDto.getPassword())) {
//            throw new BaseException(FAILED_TO_PASSWORD);
//        }

        // 입력한 비밀번호를 해싱한 후에 저장된 해시된 비밀번호와 비교
        if(!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BaseException(FAILED_TO_PASSWORD);
        }

        String jwtToken = jwtService.createJwt(user.getId());

        return LoginResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .jwtToken(jwtToken)
                .build();
    }

    //[회원 탈퇴]
    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        user.deleteUser();
    }

    //[회원 상세 정보 조회]
    @Transactional(readOnly = true)
    public GetUserDto getUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        return new GetUserDto(user);
    }

}
