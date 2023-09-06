package com.example.board.like.controller;

import com.example.board.common.response.BaseResponse;
import com.example.board.like.dto.LikeRequestDto;
import com.example.board.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "like 도메인", description = "좋아요 API")
@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요 기능
     * [Post]
     */

    @ResponseBody
    @Operation(summary = "좋아요 추가")
    @PostMapping("")
    public BaseResponse<String> updateLike(@RequestBody LikeRequestDto likeRequestDto) throws Exception {
        likeService.updateLike(likeRequestDto);

        return new BaseResponse<>("좋아요 완료");
    }
}
