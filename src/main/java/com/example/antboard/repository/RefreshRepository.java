package com.example.antboard.repository;

import com.example.antboard.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {
    Boolean existsByRefresh(String refresh);


    @Transactional
    void deleteByRefresh(String refresh);


}
