package com.spacedog.file.service;

import com.spacedog.file.domain.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    String getFullPath(String fileName);
    FileData uploadFile(MultipartFile multipartFile);
    List<FileData> uploadFiles(List<MultipartFile> multipartFiles);
    FileData getFile(Long id);
}
