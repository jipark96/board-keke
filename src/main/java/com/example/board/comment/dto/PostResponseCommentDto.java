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

    private Long boardId;
    private String username;
    private String content;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return content;
    }

}
