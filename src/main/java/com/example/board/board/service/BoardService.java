package com.example.board.board.service;

import com.example.board.board.dto.GetBoardDto;
import com.example.board.board.dto.PostBoardDto;
import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.common.exceptions.BaseException;
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
    @Transactional(readOnly = true)
    public List<GetBoardDto> getAllBoard() {
        return boardRepository.findAll().stream()
                .map(GetBoardDto::new)
                .collect(Collectors.toList());
    }

    //[글 상세 조회]
    public GetBoardDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        return new GetBoardDto(board);

    }
}
