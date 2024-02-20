package com.example.antboard.repository;

import com.example.antboard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
