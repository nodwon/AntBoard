package com.example.antboard.repository;

import com.example.antboard.entity.Board;
import com.example.antboard.entity.QBoard;
import com.example.antboard.entity.QFileEntity;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;

import java.util.List;

public class BoardRepositoryImpl implements BoardRepositoryCustom  {

    private final JPAQueryFactory queryFactory;
    private final Querydsl querydsl;


    @Autowired
    public BoardRepositoryImpl(EntityManager entityManager) {
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(Board.class, "board"));
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Board> findAllWithFiles(Pageable pageable) {
        QBoard board = QBoard.board;
        QFileEntity fileEntity = QFileEntity.fileEntity;

        JPAQuery<Board> query = queryFactory
                .selectFrom(board)
                .leftJoin(board.files, fileEntity).fetchJoin()
                .orderBy(board.id.desc());

        // Correcting the type compatibility
        query = (JPAQuery<Board>) querydsl.applyPagination(pageable, query);

        List<Board> boards = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(boards, pageable, total);
    }
}
