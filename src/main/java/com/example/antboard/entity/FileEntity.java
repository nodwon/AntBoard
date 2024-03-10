package com.example.antboard.entity;

import com.example.antboard.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(length=100000)
    private String base64Data;

    @Column(name = "s3Url")
    private String s3Url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    public Board board;

    @Builder
    public FileEntity(Long id, String FileName, String fileType,String base64Data){
        this.id= id;
        this.FileName = FileName;
        this.fileType = fileType;
        this.base64Data = base64Data;
    }
//    @Builder
//    public  FileEntity(Long id, String FileName, String fileType, String s3Url){
//        this.id= id;
//        this.FileName = FileName;
//        this.fileType = fileType;
//        this.s3Url =s3Url;
//    }


    public void setMappingBoard(Board board) {
        this.board = board;
    }



}
