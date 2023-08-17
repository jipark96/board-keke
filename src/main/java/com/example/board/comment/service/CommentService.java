package com.example.board.comment.service;

import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.comment.dto.PostCommentDto;
import com.example.board.comment.dto.PostResponseCommentDto;
import com.example.board.comment.entity.Comment;
import com.example.board.comment.repository.CommentRepository;
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
    @Transactional
    public PostResponseCommentDto createComment(PostCommentDto postCommentDto, String username, Long id) {
        User user = userRepository.findByUsername(username);
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        postCommentDto.setUser(user);
        postCommentDto.setBoard(board);
        Comment comment = postCommentDto.toEntity(user, board, null);
        Comment savedComment = commentRepository.save(comment);

        return new PostResponseCommentDto(savedComment);

    }

    //[대댓글 생성]
    @Transactional
    public PostResponseCommentDto createChildComment(PostCommentDto postCommentDto, String username, Long id, Long parentId) {
        User user = userRepository.findByUsername(username);
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        Comment parent = null;
        if (parentId != null) {
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
        }

        postCommentDto.setUser(user);
        postCommentDto.setBoard(board);
        Comment comment = postCommentDto.toEntity(user, board, parent);
        Comment savedComment = commentRepository.save(comment);

        return new PostResponseCommentDto(savedComment);
    }

    //[댓글 삭제]
    public void deleteComment(Long boardId, Long id) {
        Comment comment = commentRepository.findByBoardIdAndId(boardId,id)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }

    //[댓글 수정]
    public void updateComment(Long boardId, Long id, PostCommentDto postCommentDto) {
        Comment comment = commentRepository.findByBoardIdAndId(boardId, id)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        comment.updateComment(postCommentDto.getContent());
    }

    //[대댓글 생성]


}
