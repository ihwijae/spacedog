package com.spacedog.file.controller;

import com.spacedog.file.domain.FileData;
import com.spacedog.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class FileController {


    private final FileService fileService;

    @PostMapping("/files")
    public List<Long> uploadFile(@RequestPart List<MultipartFile> multipartFiles) {
        log.info("파일 업로드 컨트롤러");
        List<FileData> fileData = fileService.uploadFiles(multipartFiles);

        return fileData.stream()
                .map(f -> f.getId())
                .collect(Collectors.toList());
    }


}
