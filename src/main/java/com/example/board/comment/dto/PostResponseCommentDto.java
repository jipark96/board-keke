package com.example.board.comment.dto;

import com.example.board.comment.entity.Comment;
import com.example.board.user.dto.JoinResponseDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseCommentDto {
    private Long id;
    private Long boardId;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private Long parentCommentId;
    public PostResponseCommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.username = comment.getUser().getUsername();
        this.userId = comment.getUser().getId();
        this.boardId = comment.getBoard().getId();
    }
}
