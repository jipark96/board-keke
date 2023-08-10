package com.example.board.comment.dto;

import com.example.board.board.entity.Board;
import com.example.board.comment.entity.Comment;
import com.example.board.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentDto {

    private Long boardId;
    private Long userId;
    private String content;


    public Comment toEntity(Board board) {
        return Comment.builder()
                .content(this.content)
                .board(board)
                .build();
    }
}
