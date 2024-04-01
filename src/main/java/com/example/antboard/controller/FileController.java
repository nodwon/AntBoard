package com.example.antboard.controller;

import com.example.antboard.dto.response.file.FileDownloadResponseDto;
import com.example.antboard.dto.response.file.FileUploadResponseDto;
import com.example.antboard.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/board/{boardId}/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<List<FileUploadResponseDto>> upload(
            @PathVariable("boardId") Long boardId,
            @RequestParam("file") List<MultipartFile> files) throws IOException {
        List<FileUploadResponseDto> saveFile = fileService.upload(boardId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveFile);
    }

    @GetMapping("/{fileId}/image")
    public ResponseEntity<FileDownloadResponseDto> getFileImage(@PathVariable("boardId") Long boardId, @PathVariable("fileId") Long fileId) {
        FileDownloadResponseDto imageData = fileService.getFile(boardId, fileId);
        if (imageData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(imageData);
    }



    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> download(
            @RequestParam("fileId") Long fileId) throws IOException {
        FileDownloadResponseDto downloadDto = fileService.download(fileId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(downloadDto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + downloadDto.getFilename() + "\"")
                .body(new ByteArrayResource(downloadDto.getContent().getBytes()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Long> delete(@RequestParam("fileId") Long fileId) {
        fileService.delete(fileId);
        return ResponseEntity.status(HttpStatus.OK).build();


    }

}
