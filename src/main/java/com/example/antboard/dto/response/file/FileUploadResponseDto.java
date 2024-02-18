package com.example.antboard.dto.response.file;

import com.example.antboard.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileUploadResponseDto {

    private Long fileId;
    private String FileName;
    private String filePath;
    private String fileType;

    @Builder
    public FileUploadResponseDto(Long fileId, String fileName, String filePath, String fileType){
        this.fileId =fileId;
        this.FileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
    }
    public static FileUploadResponseDto from(FileEntity file){
        return FileUploadResponseDto.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .filePath(file.getFilePath())
                .fileType(file.getFileType())
                .build();
    }
}
