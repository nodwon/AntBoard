package com.example.antboard.repository;

import com.example.antboard.entity.Board;
import com.example.antboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
