package com.example.board.comment.controller;

import com.example.board.board.dto.PostBoardDto;
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
@RequestMapping("board")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성 API
     * [POST] /board/{id}/comment?username=???
     */
    @Operation(summary = "댓글 생성")
    @ResponseBody
    @PostMapping("/{id}/comment")
    public BaseResponse<String> createComment(@PathVariable Long id, @RequestBody PostCommentDto postCommentDto, @RequestParam String username) {
        commentService.createComment(postCommentDto, username, id);
        return new BaseResponse<>("댓글 완료");
    }
}
