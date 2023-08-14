package com.example.board.board.dto;

import com.example.board.board.entity.Board;
import com.example.board.user.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


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
    private MultipartFile file;
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


