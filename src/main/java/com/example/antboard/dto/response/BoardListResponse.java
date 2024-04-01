package com.example.antboard.dto.response;

import com.example.antboard.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardListResponse {
    private Long boardId;
    private String title;
    private String content;
    private String createdDate;
    private String modifiedDate;
    private List<String> imageBase64Data;

    @Builder
    public BoardListResponse(Long boardId, String title, String content, String createdDate, String modifiedDate,List<String> imageBase64Data) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.imageBase64Data = imageBase64Data;
    }

    public BoardListResponse(Board board, List<String> imageBase64Data) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.imageBase64Data = imageBase64Data;
    }

    // Entity -> DTO
    public static BoardListResponse from(Board board,List<String> imageBase64Data) {
        return BoardListResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .imageBase64Data(imageBase64Data)
                .build();
    }

}
