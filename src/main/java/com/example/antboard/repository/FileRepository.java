package com.example.antboard.repository;

import com.example.antboard.entity.Board;
import com.example.antboard.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByS3Url(String S3url);

    List<FileEntity> findByBoard(Board board);
}
