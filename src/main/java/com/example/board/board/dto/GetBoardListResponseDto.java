package com.example.board.board.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardListResponseDto {
    private List<GetBoardDto> boardList;
    private int totalCount;
}
