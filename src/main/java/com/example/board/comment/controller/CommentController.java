package com.example.board.comment.controller;

import com.example.board.board.dto.PatchBoardDto;
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
    public BaseResponse<PostResponseCommentDto> createComment(@PathVariable Long id, @RequestBody PostCommentDto postCommentDto, @RequestParam String username) {
        PostResponseCommentDto postResponseCommentDto = commentService.createComment(postCommentDto, username, id);
        return new BaseResponse<>(postResponseCommentDto);
    }

    /**
     * 댓글 삭제
     * [DELETE] /board/{id}/comment/{commentId}
     */
    @Operation(summary = "댓글 삭제")
    @ResponseBody
    @DeleteMapping("/{boardId}/comment/{id}")
    public BaseResponse<String> deleteComment(@PathVariable Long boardId, @PathVariable Long id) {
        commentService.deleteComment(boardId, id);
        return new BaseResponse<>("삭제 완료");
    }

    /**
     * 댓글 수정
     * [PATCH] /board/{id}/comment/{commentId}
     */

    @Operation(summary = "댓글 수정")
    @ResponseBody
    @PatchMapping("/{boardId}/comment/{id}")
    public BaseResponse<PostCommentDto> updateComment(@PathVariable Long boardId, @PathVariable Long id, @RequestBody PostCommentDto postCommentDto) {
        commentService.updateComment(boardId,id,postCommentDto);
        return new BaseResponse<>(postCommentDto);
    }

}
