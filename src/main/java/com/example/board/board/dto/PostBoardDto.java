package com.example.board.board.dto;

import com.example.board.board.entity.Board;
import com.example.board.user.entity.User;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostBoardDto {
    private String title;
    private String content;
    private String username;
    private int view;
    private User user;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .username(username)
                .view(0)
                .user(user)
                .build();
    }
}


