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
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    public List<FileUploadResponseDto> generateThumbnails(List<FileUploadResponseDto> files) {
        List<FileUploadResponseDto> thumbnails = new ArrayList<>();
        for (FileUploadResponseDto fileDto : files) {
            try {
                // 원본 이미지 데이터 가져오기
                FileEntity fileEntity = fileRepository.findById(fileDto.getFileId()).orElseThrow(FileNotFoundException::new);
                byte[] originalImageData = Base64.getDecoder().decode(fileEntity.getBase64Data());

                // 썸네일 생성
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Thumbnails.of(new ByteArrayInputStream(originalImageData))
                        .size(100, 100) // 원하는 썸네일 크기 지정
                        .outputFormat("jpg") // 썸네일 포맷 지정
                        .toOutputStream(outputStream); // 바이트 배열로 썸네일 생성

                // 생성된 썸네일 데이터를 Base64로 인코딩하여 DTO에 추가
                String thumbnailBase64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                fileDto.setBase64Data(thumbnailBase64);
                thumbnails.add(fileDto);
            } catch (IOException e) {
                log.error("Error generating thumbnail for file ID {}: {}", fileDto.getFileId(), e.getMessage());
                // 예외 처리
            }
        }
        return thumbnails;
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
        boardRepository.findById(boardId).orElseThrow(() ->
                new ResourceNotFoundException("Board", "boardID", String.valueOf(boardId)));
        List<FileUploadResponseDto> responseDtoList = new ArrayList<>();
        for (FileS3Dto fileS3Dto : save3File) {
            FileUploadResponseDto responseDto = FileUploadResponseDto.builder()
                    .fileId(fileS3Dto.getFileId()) // 파일의 ID
                    .fileName(fileS3Dto.getFileName()) // 파일 이름
                    .fileType(fileS3Dto.getFileType()) // 파일 유형
                    .build();
            responseDtoList.add(responseDto);

        }
        return responseDtoList; // 저장된 파일 정보를 반환

    }
    @Transactional
    public void S3delete(Long fileId) {
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new ResourceNotFoundException("File", "File Id", String.valueOf(fileId))
        );
        fileRepository.delete(file);
    }


}



