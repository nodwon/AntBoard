package com.example.antboard.entity;

import com.example.antboard.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @Builder
    public Board(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    //== 수정 Dirty Checking ==//
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
