package com.example.board.file.entity;

import com.example.board.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName; //저장된 파일명

    private String originalFileName; // 원본 파일명

    private String contentType; // 파일 타입

    private long size; // 파일 크기

    private String filePath; // 파일 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

}
