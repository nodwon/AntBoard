package com.example.antboard.entity;

import com.example.antboard.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILE")
@Getter
@NoArgsConstructor
public class FileEntity extends BaseEntity {

    @Id@GeneratedValue
    @Column(name = "FILE_ID")
    private Long id;
    @Column
    private String FileName;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_PATH")
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    public Board board;

    @Builder
    public FileEntity(Long id, String FileName, String filePath, String fileType){
        this.id= id;
        this.FileName = FileName;
        this.filePath = filePath;
        this.fileType = fileType;
    }
    public void setMappingBoard(Board board) {
        this.board = board;
    }
}
