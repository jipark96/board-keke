package com.example.board.comment.service;

import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.comment.dto.PostCommentDto;
import com.example.board.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //[댓글 생성]
    public void createComment(PostCommentDto postCommentDto) {
        Board board = boardRepository.findById(postCommentDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        commentRepository.save(postCommentDto.toEntity(board));
    }
}
