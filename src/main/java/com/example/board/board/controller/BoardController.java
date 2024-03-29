package com.example.board.board.controller;

import com.example.board.board.dto.GetBoardDto;
import com.example.board.board.dto.GetBoardListResponseDto;
import com.example.board.board.dto.PatchBoardDto;
import com.example.board.board.dto.PostBoardDto;
import com.example.board.board.service.BoardService;
import com.example.board.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;


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
    public BaseResponse<String> createBoard(@ModelAttribute PostBoardDto postBoardDto, @RequestParam String username) {
        boardService.createBoard(postBoardDto, username);
        return new BaseResponse<>("글쓰기 완료");
    }
    /**
     * 게시판 글 리스트 조회 API
     *  [GET] /board?page=0&size=8&keyword=검색어
     */
    @ResponseBody
    @Operation(summary = "글 전체 조회")
    @GetMapping("")
    public BaseResponse<GetBoardListResponseDto> getAllBoard(@RequestParam int page,
                                                             @RequestParam int size,
                                                             @RequestParam(required = false) String keyword,
                                                             @RequestParam(required = false) String sortType) {
        GetBoardListResponseDto getBoardListResponseDto = boardService.getAllBoard(page, size, keyword, sortType);

        return new BaseResponse<>(getBoardListResponseDto);
    }

    /**
     * 게시판 글 상세 조회 API
     * [GET] /board/{boardId}
     */
    @ResponseBody
    @Operation(summary = "글 상세 조회")
    @GetMapping("/{boardId}")
    public BaseResponse<GetBoardDto> getBoard(@PathVariable("boardId") Long boardId, HttpServletRequest request, HttpServletResponse response) {
        GetBoardDto getBoardDto = boardService.getBoard(boardId);
        boardService.updateView(boardId,request, response);
        return new BaseResponse<>(getBoardDto);
    }

    /**
     * 게시판 수정
     * [PATCH] /board/edit/{boardId}
     */
    @ResponseBody
    @Operation(summary = "글 수정")
    @PatchMapping("/edit/{boardId}")
    public BaseResponse<PatchBoardDto> updateBoard(@PathVariable("boardId") Long boardId, @ModelAttribute PatchBoardDto patchBoardDto) {
        if (patchBoardDto.getDeletedFiles() == null) {
            patchBoardDto.setDeletedFiles(new ArrayList<>());
        } else if (patchBoardDto.getDeletedImages() == null) {
            patchBoardDto.setDeletedImages(new ArrayList<>());
        }
        boardService.updateBoard(boardId, patchBoardDto);
        return new BaseResponse<>(patchBoardDto);
    }

    /**
     * 게시판 삭제
     * [DELETE] /board/{boardId}
     */
    @ResponseBody
    @Operation(summary = "글 삭제")
    @DeleteMapping("/{boardId}")
    public BaseResponse<String> deleteBoard(@PathVariable("boardId") Long boardId) {

        boardService.deleteBoard(boardId);
        return new BaseResponse<>("삭제 완료");
    }
}
