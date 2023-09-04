package com.example.board.board.dto;

import com.example.board.board.entity.Board;
import com.example.board.comment.dto.PostResponseCommentDto;
import com.example.board.file.dto.FileGetDto;
import com.example.board.file.entity.File;
import com.example.board.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private List<FileGetDto> fileList;


    public GetBoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.view = board.getView();

        User user = board.getUser();
        if (user != null) {
            this.username = user.getUsername();
            this.userId = user.getId();
        }
        this.createdAt = board.getCreatedAt();
        this.commentList = board.getCommentList().stream().map(PostResponseCommentDto::new).collect(Collectors.toList());

        this.fileList = board.getFileList().stream() // 게시물의 파일 리스트를 가져옴
                .map(FileGetDto::new) // FileGetDto로 변환
                .collect(Collectors.toList()); // List로 반환
    }

}
