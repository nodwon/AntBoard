package com.example.antboard.repository;

import com.example.antboard.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId")
    Page<Comment> findCommentsByBoardId(Pageable pageable, @Param("boardId")Long boardId);

    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.member m JOIN FETCH c.board b WHERE c.id = :commentId")
    Optional<Comment> findByIdWithMemberAndBoard(Long commentId);
}
