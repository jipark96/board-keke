package com.example.board.comment.dto;

import com.example.board.board.entity.Board;
import com.example.board.comment.entity.Comment;
import com.example.board.user.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCommentDto {
    private Long id;
    private User user;
    private Board board;
    private String content;
    private Long parentCommentId;


    public Comment toEntity(User user, Board board, Comment parent) {
        return Comment.builder()
                .id(id)
                .content(content)
                .user(user)
                .board(board)
                .parent(parent)
                .build();
    }

}
