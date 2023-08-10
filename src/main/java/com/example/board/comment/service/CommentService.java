package com.example.board.comment.service;

import com.example.board.board.dto.PostBoardDto;
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

//    //[댓글 생성]
//    public PostResponseCommentDto createComment(PostCommentDto postCommentDto) {
//        Board board = boardRepository.findById(postCommentDto.getBoardId())
//                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
//        Comment comment = postCommentDto.toEntity(board);
//        commentRepository.save(comment);
//
//        return PostResponseCommentDto.builder()
//                .boardId(postCommentDto.getBoardId())
//                .content(comment.getContent())
//                .createdAt(comment.getCreatedAt())
//                .build();
//    }
        //[댓글 생성]
    @Transactional
        public Long createComment(PostCommentDto postCommentDto, String username, Long id) {
            User user = userRepository.findByUsername(username);
            Board board = boardRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
            postCommentDto.setUser(user);
            postCommentDto.setBoard(board);
            Comment comment = postCommentDto.toEntity();
            commentRepository.save(comment);

            return comment.getId();

        }

    @Transactional
    public Long createBoard(PostBoardDto postBoardDto, String username) {
        /* User 정보를 가져와 dto에 담아준다. */
        User user = userRepository.findByUsername(username);
        postBoardDto.setUser(user);
        Board board = postBoardDto.toEntity();
        boardRepository.save(board);

        return board.getId();
    }

}
