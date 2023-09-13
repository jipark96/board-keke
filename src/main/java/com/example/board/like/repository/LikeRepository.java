package com.example.board.like.repository;

import com.example.board.board.entity.Board;
import com.example.board.comment.entity.Comment;
import com.example.board.like.entity.Like;
import com.example.board.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndBoard(User user, Board board);

    List<Like> findByUserId(Long userId);
}
