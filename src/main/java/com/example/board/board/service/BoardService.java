package com.example.board.board.service;

import com.example.board.board.dto.GetBoardDto;
import com.example.board.board.dto.GetBoardListResponseDto;
import com.example.board.board.dto.PatchBoardDto;
import com.example.board.board.dto.PostBoardDto;
import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.common.response.BaseResponse;
import com.example.board.user.entity.User;
import com.example.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //[글 생성]
    @Transactional
    public Long createBoard(PostBoardDto postBoardDto, String username) {
        /* User 정보를 가져와 dto에 담아준다. */
        User user = userRepository.findByUsername(username);
        postBoardDto.setUser(user);
        Board board = postBoardDto.toEntity();
        boardRepository.save(board);

        return board.getId();
    }




    //[글 전체 조회]
    @Transactional(readOnly = true)
    public GetBoardListResponseDto getAllBoard(int page) {
        PageRequest pageRequest = PageRequest.of(page,8, Sort.by(Sort.Direction.DESC,"id"));
        Slice<Board> boardSlice = boardRepository.findAll(pageRequest);
        List<GetBoardDto> getBoardDtoList = boardSlice.map(GetBoardDto::new).getContent();

        int totalCount = boardRepository.findAll().size();

        return new GetBoardListResponseDto(getBoardDtoList, totalCount);
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
        boardRepository.delete(board);
    }

    //[조회수 증가]
    @Transactional
    public int updateView(Long boardId) {
        return boardRepository.updateView(boardId);
    }

}
