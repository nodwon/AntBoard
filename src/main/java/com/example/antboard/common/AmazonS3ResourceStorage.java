package com.example.antboard.common;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {

    private final AmazonS3 amazonS3; // AmazonS3 인터페이스를 주입 받음

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    @Value("${project.folderPath}")
    private String FOLDER_PATH;

    public List<String> save(String fullPath, List<MultipartFile> files) {
        List<String> uploadedUrls = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            fullPath = FOLDER_PATH + "/" + multipartFile.getOriginalFilename();
            File saveFile = new File(FileUtils.getLocalHomeDirectory(), fullPath);
            try {
                multipartFile.transferTo(saveFile);
                amazonS3.putObject(new PutObjectRequest(bucketName, fullPath, saveFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                String uploadedUrl = amazonS3.getUrl(bucketName, FOLDER_PATH + "/" + multipartFile.getOriginalFilename()).toString();
                uploadedUrls.add(uploadedUrl);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new ErrorException("이미지 업로드 실패");
            } finally {
                if (saveFile.exists()) removeNewFile(saveFile);
            }
        }
        return uploadedUrls;
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) log.info("The file has been deleted.");
        else {
            log.info("Failed to delete file.");
        }
    }

    public void delete(String fileUrl) {
        try {
            String key = fileUrl.substring(64);
            try {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
            } catch (AmazonServiceException e) {
                log.error(e.getErrorMessage());
                exit(1);
            }
            log.info(String.format("[%s] deletion complete", key));
        } catch (Exception e) {
            throw new ErrorException("파일삭제실패!!");
        }
    }
}


