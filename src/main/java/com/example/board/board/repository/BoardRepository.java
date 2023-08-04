package com.example.board.board.repository;

import com.example.board.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT COUNT(b) FROM Board b")
    int getTotalBoardCount();
}
