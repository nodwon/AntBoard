package com.example.antboard.entity;

import com.example.antboard.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Base64;

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

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(length=100000)
    private byte[] base64Data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    public Board board;

    @Builder
    public FileEntity(Long id, String FileName, String filePath, String fileType,byte[] base64Data){
        this.id= id;
        this.FileName = FileName;
        this.fileType = fileType;
        this.base64Data = base64Data;
    }
    public void setMappingBoard(Board board) {
        this.board = board;
    }
    // Base64 데이터를 byte 배열로 디코딩하여 반환
    public byte[] getDecodedData() {
        return Base64.getDecoder().decode(this.base64Data);
    }
}
