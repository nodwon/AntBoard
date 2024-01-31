package com.example.antboard.dto.request.board;

import com.example.antboard.entity.Board;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {
    @NotEmpty
    private String title;
    private String content;
    public BoardDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
    @Builder
    public static Board ofEntity(BoardDto dto) {
        return Board.builder()
                .title(dto.title)
                .content(dto.content)
                .build();
    }
}
