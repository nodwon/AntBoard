package com.example.antboard.dto.response.file;

import com.example.antboard.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileDownloadResponseDto {
    private String filename;
    private String fileType;
    private String content;

    @Builder
    public FileDownloadResponseDto(String filename, String fileType, String content) {
        this.filename = filename;
        this.fileType = fileType;
        this.content = content;
    }

    public static FileDownloadResponseDto from(FileEntity file, String contentType, String content) {
        return FileDownloadResponseDto.builder()
                .filename(file.getFileName())
                .fileType(contentType)
                .content(content)
                .build();
    }
}
