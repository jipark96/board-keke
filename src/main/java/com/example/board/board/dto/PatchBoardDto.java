package com.example.board.board.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchBoardDto {

    private String title;
    private String content;
    private List<String> deleted; // 기존 첨부 파일 중 삭제할 파일 목록

    @JsonIgnore
    private List<MultipartFile> files;
}
