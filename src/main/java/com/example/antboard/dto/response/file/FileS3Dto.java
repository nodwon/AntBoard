package com.example.antboard.dto.response.file;

import com.example.antboard.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileS3Dto {
    private Long fileId;
    private String FileName;
    private String fileType;
    private String S3url;

    @Builder
    public FileS3Dto(Long fileId, String fileName, String fileType, String S3url) {
        this.fileId = fileId;
        this.FileName = fileName;
        this.fileType = fileType;
        this.S3url = S3url;

    }

    public static FileS3Dto from(FileEntity file) {
        return FileS3Dto.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .build();
    }
}
