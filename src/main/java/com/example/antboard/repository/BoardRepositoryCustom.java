package com.example.antboard.repository;

import com.example.antboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom  {
    Page<Board> findAllWithFiles(Pageable pageable);
}
