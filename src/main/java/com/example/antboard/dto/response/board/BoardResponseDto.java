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
    private String writerName;
    private String createdDate;
    private String modifiedDate;

    @Builder
    public BoardResponseDto(Long boardId, String title, String content,String writerName,String createdDate,String modifiedDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
    public static BoardResponseDto from(Board board ,String writerName) {
        return BoardResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writerName(writerName)
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }

}
