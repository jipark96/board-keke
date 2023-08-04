package com.example.board.board.controller;

import com.example.board.board.dto.GetBoardDto;
import com.example.board.board.dto.PostBoardDto;
import com.example.board.board.service.BoardService;
import com.example.board.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "board 도메인", description = "게시판 API")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시판 글 생성 API
     * [POST] /board
     */
    @ResponseBody
    @Operation(summary = "글 생성")
    @PostMapping("")
    public BaseResponse<String> createBoard(@RequestBody PostBoardDto postBoardDto) {
        boardService.createBoard(postBoardDto);
        return new BaseResponse<>("글쓰기 완료");
    }

    /**
     * 게시판 글 리스트 조회 API
     * [GET] /board
     */
    @ResponseBody
    @Operation(summary = "글 전체 조회")
    @GetMapping("")
    public BaseResponse<List<GetBoardDto>> getAllBoard() {
        List<GetBoardDto> boardDtoList = boardService.getAllBoard();
        return new BaseResponse<>(boardDtoList);
    }

    /**
     * 게시판 글 상세 조회 API
     * [GET] /board/{boardId}
     */
    @ResponseBody
    @Operation(summary = "글 상세 조회")
    @GetMapping("/{boardId}")
    public BaseResponse<GetBoardDto> getBoard(@PathVariable("boardId") Long boardId) {
        GetBoardDto getBoardDto = boardService.getBoard(boardId);
        return new BaseResponse<>(getBoardDto);
    }
}
