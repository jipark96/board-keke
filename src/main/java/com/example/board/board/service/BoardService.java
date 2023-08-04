package com.example.board.board.service;

import com.example.board.board.dto.GetBoardDto;
import com.example.board.board.dto.PostBoardDto;
import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    //[글 생성]
    public void createBoard(PostBoardDto postBoardDto) {
        Board board = postBoardDto.toEntity();
        boardRepository.save(board).getId();
    }

    //[글 전체 조회]
    public List<GetBoardDto> getAllBoard() {
        return boardRepository.findAll().stream()
                .map(GetBoardDto::new)
                .collect(Collectors.toList());
    }
}
