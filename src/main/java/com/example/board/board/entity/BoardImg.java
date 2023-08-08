package com.example.board.board.entity;

import com.example.board.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardImg_id", nullable = false, updatable = false)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String imgSrc;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
