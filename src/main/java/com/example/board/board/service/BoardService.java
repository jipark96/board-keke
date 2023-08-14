package com.example.board.board.service;

import com.example.board.board.dto.GetBoardDto;
import com.example.board.board.dto.GetBoardListResponseDto;
import com.example.board.board.dto.PatchBoardDto;
import com.example.board.board.dto.PostBoardDto;
import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.file.entity.File;
import com.example.board.file.repository.FileRepository;
import com.example.board.user.entity.User;
import com.example.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;


    //[글 생성]
    @Transactional
    public Long createBoard(PostBoardDto postBoardDto, String username) {
        /* User 정보를 가져와 dto에 담아준다. */
        User user = userRepository.findByUsername(username);
        postBoardDto.setUser(user);
        Board board = postBoardDto.toEntity();
        boardRepository.save(board);

        // 파일을 저장하고 File 엔티티 생성 후 Board와 연결
        MultipartFile file = postBoardDto.getFile();
        if (file != null && !file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String filePath = "/absolute/path/to/your/project/src/main/resources/uploadedFiles/";

            File newFile = File.builder()
                    .fileName(fileName)
                    .originalFileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .filePath(filePath)
                    .board(board)
                    .build();
            fileRepository.save(newFile);
        }

        return board.getId();
    }



    //[글 전체 조회]
    @Transactional(readOnly = true)
    public GetBoardListResponseDto getAllBoard(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));
        Page<Board> boardPage;

        int totalCount;

        if (keyword == null) {
            boardPage = boardRepository.findAll(pageable);
            totalCount = boardRepository.findAll().size();
        } else {
            boardPage = boardRepository.findByTitleContaining(keyword, pageable);
            totalCount = boardRepository.countByTitleContaining(keyword);
        }

        List<GetBoardDto> getBoardDtoList = boardPage.get().map(GetBoardDto::new).collect(Collectors.toList());

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
