package com.spacedog.file.domain;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class FileData {


    private Long id;
    private String uploadFilename; // 사용자가 업로드한 파일명
    private String storeFilename; // 서버에 저장되는 파일명




    @Builder
    public FileData(Long id, String uploadFilename, String storeFilename) {
        this.id = id;
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
    }

    public FileData() {
    }
}
