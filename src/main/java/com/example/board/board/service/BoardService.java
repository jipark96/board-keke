package com.example.board.board.service;

import com.example.board.board.dto.GetBoardDto;
import com.example.board.board.dto.PatchBoardDto;
import com.example.board.board.dto.PostBoardDto;
import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
//    @Transactional(readOnly = true)
//    public List<GetBoardDto> getAllBoard() {
//        return boardRepository.findAll().stream()
//                .map(GetBoardDto::new)
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public List<GetBoardDto> getAllBoard(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.getContent().stream()
                .map(GetBoardDto::new)
                .collect(Collectors.toList());
    }

    //[글 상세 조회]
    @Transactional(readOnly = true)
    public GetBoardDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        return new GetBoardDto(board);

    }

    //[글 수정]
    public void updateBoard(Long boardId, PatchBoardDto patchBoardDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        board.updateBoard(patchBoardDto.getTitle(), patchBoardDto.getContent());
    }

    //[글 삭제]
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
//        board.deleteBoard();
        boardRepository.delete(board);
    }
}
