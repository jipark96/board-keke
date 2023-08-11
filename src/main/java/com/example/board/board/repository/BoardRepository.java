package com.example.board.board.repository;

import com.example.board.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("update  Board b set b.view = b.view + 1 where b.id = :id")
    int updateView(Long id);
}
