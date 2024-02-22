package com.example.antboard.service;

import com.amazonaws.SdkBaseException;
import com.example.antboard.common.AmazonS3ResourceStorage;
import com.example.antboard.common.FileUtils;
import com.example.antboard.common.ResourceNotFoundException;
import com.example.antboard.entity.Board;
import com.example.antboard.repository.BoardRepository;
import com.example.antboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileS3UploadService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Transactional
    public List<String> upload(Long boardId, List<MultipartFile> files) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new ResourceNotFoundException("Board", "boardID", String.valueOf(boardId)));
        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile multipartFile : files) {
            String fullPath = FileUtils.createPath(multipartFile);
            List<String> urls = amazonS3ResourceStorage.save(fullPath, files);
            uploadedUrls.addAll(urls);
        }

        return uploadedUrls;
    }

    public void delete(String fileUrl) throws SdkBaseException {
        amazonS3ResourceStorage.delete(fileUrl);
    }

}
