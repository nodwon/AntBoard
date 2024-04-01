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
    private String imageBase64Data;

    @Builder
    public BoardDetailsFileResponseDto(Long fileId, String FileName, String fileType,String imageBase64Data) {
        this.fileId = fileId;
        this.FileName = FileName;
        this.fileType = fileType;
        this.imageBase64Data = imageBase64Data;
    }

    public static BoardDetailsFileResponseDto from(FileEntity file) {
        return BoardDetailsFileResponseDto.builder()
                .fileId(file.getId())
                .FileName(file.getFileName())
                .fileType(file.getFileType())
                .imageBase64Data(file.getBase64Data())
                .build();
    }
}

