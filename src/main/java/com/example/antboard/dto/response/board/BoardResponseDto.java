package com.example.antboard.dto.response.board;

import com.example.antboard.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private String createdDate;
    @Builder
    public BoardResponseDto(Long boardId, String title, String content,String createdDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
    }
    public static BoardResponseDto from(Board board) {
        return BoardResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .build();
    }

}
