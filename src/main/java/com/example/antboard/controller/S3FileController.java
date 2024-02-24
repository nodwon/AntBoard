package com.example.antboard.controller;

import com.example.antboard.dto.response.file.FileS3Dto;
import com.example.antboard.dto.response.file.FileUploadResponseDto;
import com.example.antboard.service.FileS3UploadService;
import com.example.antboard.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/board/{boardId}/file")
@RequiredArgsConstructor
public class S3FileController {
    private final FileService fileService;
    private final FileS3UploadService fileS3UploadService;

    @PostMapping("/S3upload")
    public ResponseEntity<List<FileUploadResponseDto>> upload(
            @PathVariable("boardId") Long boardId,
            @RequestParam("file")List<MultipartFile> files) throws IOException{
        List<FileS3Dto> save3File = fileS3UploadService.S3uploadFile(files);
        List<FileUploadResponseDto> saveFile = fileService.saveS3(boardId,save3File);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveFile);
    }
    @DeleteMapping("/S3delete")
    public ResponseEntity<Long> delete(@RequestParam("fileId") Long fileId){
        fileService.delete(fileId);
        return ResponseEntity.status(HttpStatus.OK).build();


    }
}
