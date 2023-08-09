package com.example.board.comment.service;

import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.comment.dto.PostCommentDto;
import com.example.board.comment.dto.PostResponseCommentDto;
import com.example.board.comment.entity.Comment;
import com.example.board.comment.repository.CommentRepository;
import com.example.board.user.dto.LoginResponseDto;
import com.example.board.user.entity.User;
import com.example.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //[댓글 생성]
    public PostResponseCommentDto createComment(PostCommentDto postCommentDto) {
        Board board = boardRepository.findById(postCommentDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        Comment comment = postCommentDto.toEntity(board);
        commentRepository.save(comment);

        return PostResponseCommentDto.builder()
                .boardId(postCommentDto.getBoardId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
