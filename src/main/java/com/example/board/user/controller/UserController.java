package com.example.board.user.controller;

import static com.example.board.common.response.BaseResponseStatus.*;
import static com.example.board.utils.ValidationRegex.*;

import com.example.board.board.dto.GetBoardListResponseDto;
import com.example.board.common.response.BaseResponse;
import com.example.board.user.dto.*;
import com.example.board.user.service.UserService;
import com.example.board.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "user 도메인", description = "회원가입 API, 로그인 API, 유저 정보 조회 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 회원가입 API
     * [POST] /user
     */
    @ResponseBody
    @Operation(summary = "회원가입")
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

    /**
     * 아이디 중복 확인 API
     * [GET] /user/check/{username}
     */
    @ResponseBody
    @Operation(summary = "아이디 중복 확인")
    @GetMapping("/check/{username}")
    public BaseResponse<Boolean> checkUsername(@PathVariable("username") String username) {
        boolean exists = userService.isUsernameExists(username);
        return new BaseResponse<>(exists);
    }

    /**
     * 회원조회 API
     * [GET] /user
     */
    @ResponseBody
    @Operation(summary = "회원 전체 조회")
    @GetMapping("")
    public BaseResponse<List<GetUserDto>> getUsers() {
        List<GetUserDto> getUserDto = userService.getUsers();
        return new BaseResponse<>(getUserDto);
    }

    /**
     * 로그인 API
     * [POST] /user/login
     */
    @ResponseBody
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public BaseResponse<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = userService.loginUser(loginRequestDto);
        return new BaseResponse<>(loginResponseDto);
    }

    /**
     * 회원 탈퇴 API
     * [DELETE] /user/{userId}
     */
    @ResponseBody
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/{userId}")
    public BaseResponse<String> deleteUser(@PathVariable("userId") Long userId) {

        userService.deleteUser(userId);

        String result = "삭제 완료";
        return new BaseResponse<>(result);
    }

    /**
     * 회원 상세 조회
     * [GET] /user/{userId}
     */
    @ResponseBody
    @Operation(summary = "회원 상세 조회")
    @GetMapping("/{userId}")
    public BaseResponse<GetUserDto> getUser(@PathVariable("userId") Long userId) {
        GetUserDto getUserDto = userService.getUser(userId);
        return new BaseResponse<>(getUserDto);
    }

    /**
     * 회원 정보 수정
     * [PATCH] /user/edit/{userId}
     */
    @ResponseBody
    @Operation(summary = "회원 정보 수정")
    @PatchMapping("edit/{userId}")
    public BaseResponse<PatchUserDto> modifyUser(@PathVariable("userId") Long userId, @RequestBody PatchUserDto patchUserDto) {
        Long jwtUserId = jwtService.getUserId();

        userService.modifyUser(userId, patchUserDto);
        return new BaseResponse<>(patchUserDto);
    }

    /**
     * 자기 글 리스트 조회
     * [GET] /{userId}/board
     */
    @ResponseBody
    @Operation(summary = "자기 글 리스트 조회")
    @GetMapping("/{userId}/board")
    public BaseResponse<GetBoardListResponseDto> getUserBoard(@PathVariable Long userId,
                                                              @RequestParam int page,
                                                              @RequestParam int size,
                                                              @RequestParam(required = false) String keyword,
                                                              @RequestParam(required = false) String sortType) {
        GetBoardListResponseDto boards = userService.getUserBoard(userId, page, size, keyword, sortType);
        return new BaseResponse<>(boards);
    }

}
