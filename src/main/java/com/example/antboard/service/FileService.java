package com.example.antboard.service;

import com.example.antboard.common.ResourceNotFoundException;
import com.example.antboard.dto.response.file.FileDownloadResponseDto;
import com.example.antboard.dto.response.file.FileS3Dto;
import com.example.antboard.dto.response.file.FileUploadResponseDto;
import com.example.antboard.entity.Board;
import com.example.antboard.entity.FileEntity;
import com.example.antboard.repository.BoardRepository;
import com.example.antboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;


    @Transactional
    public List<FileUploadResponseDto> upload(Long boardId, List<MultipartFile> files) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new ResourceNotFoundException("Board", "boardID", String.valueOf(boardId)));
        List<FileEntity> fileEntities = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            // create File Entity & 연관관게 매핑
            FileEntity saveFile = FileEntity.builder()
                    .FileName(multipartFile.getOriginalFilename())
                    .fileType(multipartFile.getContentType())
                    .build();
            saveFile.setMappingBoard(board);
            // 파일 데이터를 Base64로 인코딩하여 저장
            saveFile.setBase64Data(Base64.getEncoder().encodeToString(multipartFile.getBytes()));
            // File Entity 저장 및 DTO로 변환 전송

            fileEntities.add(fileRepository.save(saveFile));
        }

        return fileEntities.stream()
                .map(FileUploadResponseDto::from)
                .collect(Collectors.toList());

    }


    @Transactional
    public FileDownloadResponseDto download(Long fileId) throws IOException {

        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                FileNotFoundException::new
        );
        String contentType = determineContentType(file.getFileType());
        String content = file.getBase64Data();
        return FileDownloadResponseDto.from(file, contentType, content);
    }

    public void delete(Long fileId) {
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new ResourceNotFoundException("File", "File Id", String.valueOf(fileId))
        );
//        byte[] content = file.getDecodedData();

        fileRepository.delete(file);
    }

    private String determineContentType(String contentType) {
        // ContentType에 따라 MediaType 결정
        return switch (contentType) {
            case "image/png" -> MediaType.IMAGE_PNG_VALUE;
            case "image/jpeg" -> MediaType.IMAGE_JPEG_VALUE;
            case "text/plain" -> MediaType.TEXT_PLAIN_VALUE;
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };
    }

@Transactional
public List<FileUploadResponseDto> saveS3(Long boardId, List<FileS3Dto> save3File) {
    Board board = boardRepository.findById(boardId).orElseThrow(() ->
            new ResourceNotFoundException("Board", "boardID", String.valueOf(boardId)));

    List<FileEntity> fileEntities = new ArrayList<>();
    for (FileS3Dto fileS3Dto : save3File) {
        // FileEntity 생성 및 초기화
        FileEntity fileEntity = FileEntity.builder()
                .FileName(fileS3Dto.getFileName())
                .fileType(fileS3Dto.getFileType())
//                .s3Url(fileS3Dto.getS3url()) // S3 URL을 포함시키는 것으로 가정
                .build();

        fileEntity.setMappingBoard(board); // Board와의 연관관계 설정
        fileRepository.save(fileEntity); // 파일 엔티티 저장
        fileEntities.add(fileEntity);
    }

    // 저장된 FileEntity 객체들을 DTO로 변환하여 반환
    return fileEntities.stream()
            .map(file -> new FileUploadResponseDto(
                    file.getId(),
                    file.getFileName(),
                    file.getFileType(),
                    file.getS3Url()
            ))
            .collect(Collectors.toList());
}


    @Transactional
    public void S3delete(Long fileId) {
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new ResourceNotFoundException("File", "File Id", String.valueOf(fileId))
        );
        fileRepository.delete(file);
    }

    @Transactional
    public FileDownloadResponseDto getFile(Long boardId, Long fileId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "boardId", String.valueOf(boardId)));

        FileEntity file = board.getFiles().stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("File", "fileId", String.valueOf(fileId)));
        String contentType = determineContentType(file.getFileType());
        String content = file.getBase64Data();
        return FileDownloadResponseDto.from(file, contentType, content);
    }
}



