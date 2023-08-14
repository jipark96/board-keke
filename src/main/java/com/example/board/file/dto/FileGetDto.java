package com.example.board.file.dto;

import com.example.board.file.entity.File;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileGetDto {
    private Long fileId;
    private String fileName;
    private String filePath;

    public FileGetDto(File file) {
        this.fileId = file.getId();
        this.fileName = file.getFileName();
        this.filePath = file.getFilePath();
    }
}