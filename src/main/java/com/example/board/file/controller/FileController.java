package com.example.board.file.controller;


import com.example.board.common.response.BaseResponse;
import com.example.board.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "file 도메인", description = "파일 API")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 파일 다운로드
     * [GET] /board/{boardId}
     */
    @ResponseBody
    @Operation(summary = "파일 다운로드")
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        return fileService.downloadFile(fileId);
    }
}
