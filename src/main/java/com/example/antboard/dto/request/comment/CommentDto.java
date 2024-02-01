package com.example.antboard.dto.request.comment;

import com.example.antboard.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private String content;

    @Builder
    public CommentDto(String content) {
        this.content = content;
    }

    public static Comment of(CommentDto dto) {
        return Comment.builder()
                .content(dto.getContent())
                .build();
    }
}
