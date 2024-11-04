package com.spacedog.file.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "file_data")
public class FileData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String uploadFileName; // 사용자가 업로드한 파일명

    private String storeFileName; // 서버에 저장되는 파일명

    private String filePath;

    private Long itemId;




    @Builder
    public FileData(Long id, String uploadFilename, String storeFilename, String filePath, Long itemId) {
        this.id = id;
        this.uploadFileName = uploadFilename;
        this.storeFileName = storeFilename;
        this.filePath = filePath;
        this.itemId = itemId;
    }

    public FileData() {
    }
}
