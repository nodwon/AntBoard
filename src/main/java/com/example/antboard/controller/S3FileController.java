package com.example.antboard.controller;

import com.example.antboard.service.FileS3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class S3FileController {
    private final FileS3UploadService fileS3UploadService;

    public ResponseEntity<List<String>> upload(
            @PathVariable("boardId") Long boardId,
            @RequestParam("file")List<MultipartFile> files) throws IOException {
        List<String> saveFile = fileS3UploadService.upload(boardId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveFile);
    }
}
