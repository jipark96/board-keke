package com.example.board.comment.repository;

import com.example.board.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByBoardIdAndId(Long boardId, Long id);
}
