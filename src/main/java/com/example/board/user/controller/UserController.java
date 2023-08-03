package com.example.board.user.controller;

import static com.example.board.common.response.BaseResponseStatus.*;
import static com.example.board.utils.ValidationRegex.*;

import com.example.board.common.response.BaseResponse;
import com.example.board.user.dto.JoinRequestDto;
import com.example.board.user.dto.JoinResponseDto;
import com.example.board.user.service.UserService;
import com.example.board.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 회원가입 API
     * [POST] /users
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<JoinResponseDto> createUser(@RequestBody JoinRequestDto joinRequestDto) {
        if(joinRequestDto.getEmail() == null) {
            return new BaseResponse<>(USERS_EMPTY_EMAIL);
        }
        if(!isRegexEmail(joinRequestDto.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        JoinResponseDto joinResponseDto = userService.createUser(joinRequestDto);
        return new BaseResponse<>(joinResponseDto);

    }

}
