package com.example.antboard.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkBaseException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.antboard.common.ResourceNotFoundException;
import com.example.antboard.dto.response.file.FileS3Dto;
import com.example.antboard.entity.FileEntity;
import com.example.antboard.repository.BoardRepository;
import com.example.antboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileS3UploadService {
    private final FileRepository fileRepository;
    private final AmazonS3 amazonS3; // AmazonS3 인터페이스를 주입 받음
    private final BoardRepository boardRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    @Transactional
    public List<FileS3Dto> S3uploadFile(List<MultipartFile> multipartFiles) throws IOException {

        List<FileS3Dto> fileList = new ArrayList<>();

        try {
            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = generateFileName(multipartFile); // 파일 이름 생성
                String contentType = determineContentType(Objects.requireNonNull(multipartFile.getContentType()));
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(contentType);
                metadata.setContentLength(multipartFile.getSize()); // 스트림의 길이 설정


                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, multipartFile.getInputStream(), metadata));


                // 업로드된 파일의 URL을 반환할 DTO 생성
                FileS3Dto fileS3Dto = new FileS3Dto();
                fileS3Dto.setFileName(fileName);
                fileS3Dto.setS3url(amazonS3.getUrl(bucketName, fileName).toString());

                // DTO 리스트에 추가
                fileList.add(fileS3Dto);
            }
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return fileList;
    }

    private String generateFileName(MultipartFile multipartFile) {
        // 파일 이름 생성 로직 구현
        // 예시: UUID.randomUUID()를 사용하여 고유한 파일 이름 생성
        return UUID.randomUUID().toString();
    }

    public void delete(String fileUrl) throws SdkBaseException {
        FileEntity file = fileRepository.findByS3Url(fileUrl).orElseThrow(
                () -> new ResourceNotFoundException("File", "File Id", String.valueOf(fileUrl))
        );
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
}
