package com.example.antboard.dto.response.board;

import com.example.antboard.dto.response.file.BoardDetailsFileResponseDto;
import com.example.antboard.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardDetailResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private String createdDate;
    private String modifiedDate;
    private List<BoardDetailsFileResponseDto> files;

    @Builder
    public BoardDetailResponseDto(Long boardId, String title, String content, String createdDate, String modifiedDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
    public static BoardDetailResponseDto from(Board board) {
        return BoardDetailResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }

}
