package com.example.antboard.dto.response.file;

import com.example.antboard.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BoardDetailsFileResponseDto {

    private Long fileId;
    private String FileName;
    private String fileType;

    @Builder
    public BoardDetailsFileResponseDto(Long fileId, String FileName, String fileType) {
        this.fileId = fileId;
        this.FileName = FileName;
        this.fileType = fileType;
    }

    public static BoardDetailsFileResponseDto from(FileEntity file) {
        return BoardDetailsFileResponseDto.builder()
                .fileId(file.getId())
                .FileName(file.getFileName())
                .fileType(file.getFileType())
                .build();
    }
}

