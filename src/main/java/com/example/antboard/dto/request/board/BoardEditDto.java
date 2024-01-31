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
public class BoardEditDto {
    @NotEmpty
    private String title;
    private String content;
    @Builder
    public BoardEditDto(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
