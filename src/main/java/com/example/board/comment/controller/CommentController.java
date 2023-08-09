package com.example.board.comment.controller;

import com.example.board.comment.dto.PostCommentDto;
import com.example.board.comment.dto.PostResponseCommentDto;
import com.example.board.comment.service.CommentService;
import com.example.board.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment 도메인", description = "댓글 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성 API
     * [POST] /comment
     */
    @Operation(summary = "댓글 생성")
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostResponseCommentDto> createComment(@RequestBody PostCommentDto postCommentDto) {
        PostResponseCommentDto postResponseCommentDto = commentService.createComment(postCommentDto);
        return new BaseResponse<>(postResponseCommentDto);
    }

}
