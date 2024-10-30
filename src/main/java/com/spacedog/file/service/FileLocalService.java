package com.spacedog.file.service;

import com.spacedog.file.domain.FileData;
import com.spacedog.file.ex.FileException;
import com.spacedog.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileLocalService implements FileService {

    private final FileRepository fileRepository;


    @Value("${file.dir}")
    private String fileDir;


    @Override
    public String getFullPath(String fileName) {
        log.info("fileDir: {}", fileDir);
        return fileDir + fileName;
    }

    @Override
    public List<FileData> uploadFiles(List<MultipartFile> multipartFiles) {
        List<FileData> files = new ArrayList<>();

        multipartFiles.forEach(f -> {
            if(!multipartFiles.isEmpty()) {
                FileData fileData = uploadFile(f);
                files.add(fileData);
            }
        });
        return files;
    }

    @Override
    public FileData uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 사용자가 업로드한 파일명
        String originalFilename = multipartFile.getOriginalFilename();
        log.info("originalFilename: {}", originalFilename);

        // 서버에 저장할 이름
        String storeFileName = createStoreFileName(originalFilename);
        log.info("storeFileName: {}", storeFileName);

        try {
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
        } catch (IOException e) {
            throw new FileException("파일을 업로드 할 수 없습니다", e);
        }

        FileData fileData = FileData.builder()
                .uploadFilename(originalFilename)
                .storeFilename(storeFileName)
                .build();

        return fileRepository.save(fileData);


    }

    /** 서버에 저장할 이름 추출 **/
    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }


    /** 확장자만 떼오는 메서드 **/
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }




}
