package com.example.board.like.service;

import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.like.dto.LikeRequestDto;
import com.example.board.like.entity.Like;
import com.example.board.like.repository.LikeRepository;
import com.example.board.user.entity.User;
import com.example.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //[좋아요 증가, 감소]
    @Transactional
    public void updateLike (LikeRequestDto likeRequestDto) throws Exception {
        User user = userRepository.findById(likeRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        Board board = boardRepository.findById(likeRequestDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        //[유저가 해당 게시글에 좋아요 눌렀는지 확인]
        Optional<Like> likeOptional = likeRepository.findByUserAndBoard(user, board);

        //[이미 좋아요 눌렀을 경우 -> 좋아요 취소 처리]
        if (likeOptional.isPresent()) {
            likeRepository.delete(likeOptional.get());
            board.decreaseLikeCount();
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setBoard(board);
            likeRepository.save(like);
            board.increaseLikeCount();
        }

        boardRepository.save(board);
    }
}
