package com.example.board.comment.entity;

import com.example.board.board.entity.Board;
import com.example.board.comment.dto.PostCommentDto;
import com.example.board.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board board;

    public void setBoard(Board board) {
        this.board = board;
    }

    @Builder
    public Comment(Long id, String content, Board board) {
        this.id = id;
        this.content = content;
        this.board = board;
    }

    public void makeComment(PostCommentDto postCommentDto, Board board) {
        this.board = board;
        this.content = postCommentDto.getContent();
    }
}