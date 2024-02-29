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
    private String base64Data;
    private String fileType;
    private String S3url;

    @Builder
    public FileUploadResponseDto(Long fileId, String fileName, String base64Data, String fileType){
        this.fileId =fileId;
        this.FileName = fileName;
        this.base64Data = base64Data;
        this.fileType = fileType;
    }

    public static FileUploadResponseDto from(FileEntity file){
        return FileUploadResponseDto.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .base64Data(file.getBase64Data())
                .fileType(file.getFileType())
                .build();
    }


}
