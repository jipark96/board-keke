package com.example.board.board.repository;

import com.example.board.board.entity.Board;
import com.example.board.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {

    //[조회수 증가]
    @Modifying
    @Query("update  Board b set b.view = b.view + 1 where b.id = :id")
    int updateView(Long id);

    //[검색] Containing => Like => %{keyword}%
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    // title 필드에 keyword가 포함된 게시물의 수를 반환하는 메서드
    int countByTitleContaining(String keyword);

    // 자기 글 조회
    Page<Board> findByUserId(Long userId, Pageable pageable);
    Page<Board> findByUserIdAndTitleContaining(Long userId, String keyword, Pageable pageable);

    List<Board> findByIdIn(List<Long> ids);

}
