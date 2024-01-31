package com.example.antboard.dto.response;

import com.example.antboard.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardListResponse {
    private Long boardId;
    private String title;
    private String content;
    private String createdDate;
    private String modifiedDate;
    @Builder
    public BoardListResponse(Long boardId, String title, String content, String createdDate, String modifiedDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
    // Entity -> DTO
    public static BoardListResponse from(Board board) {
        return BoardListResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }

}
