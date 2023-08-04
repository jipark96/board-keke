package com.example.board.board.dto;

import com.example.board.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;

    public GetBoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = (board.getUser() != null) ? board.getUser().getUsername() : null; // 수정 필요
        this.createdAt = board.getCreatedAt();
    }
}
