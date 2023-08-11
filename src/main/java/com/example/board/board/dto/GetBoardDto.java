package com.example.board.board.dto;

import com.example.board.board.entity.Board;
import com.example.board.comment.dto.PostResponseCommentDto;
import com.example.board.comment.entity.Comment;
import com.example.board.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private int view;
    private Long userId;
    private LocalDateTime createdAt;
    private List<PostResponseCommentDto> commentList;

    public GetBoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.view = board.getView();
        this.username = board.getUser().getUsername();
        this.userId = board.getUser().getId();
        this.createdAt = board.getCreatedAt();
        this.commentList = board.getCommentList().stream().map(PostResponseCommentDto::new).collect(Collectors.toList());
    }

}
