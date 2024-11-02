package com.spacedog.file.controller;

import com.spacedog.file.domain.FileData;
import com.spacedog.file.service.FileService;
import com.spacedog.global.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class FileController {


    private final FileService fileService;

    @PostMapping("/files/{itemId}")
    public ApiResponse<List<String>> uploadFile(@PathVariable Long itemId, @RequestPart List<MultipartFile> multipartFiles) {
        log.info("파일 업로드 컨트롤러");
        List<FileData> fileData = fileService.uploadFiles(itemId, multipartFiles);


        List<String> resultPath = fileData.stream()
                .map(f -> f.getFilePath())
                .collect(Collectors.toList());

        return ApiResponse.success(resultPath, "파일 업로드 완료");
    }


    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws IOException {
        log.info("filename: {}", filename);
        UrlResource urlResource = new UrlResource("file:" + fileService.getFullPath(filename));

        String contentType =
                Files.probeContentType(Paths.get(fileService.getFullPath(filename)));

        MediaType mediaType;
        if (contentType != null) {
            mediaType = MediaType.parseMediaType(contentType);
        } else {
            mediaType = MediaType.APPLICATION_OCTET_STREAM; // 기본값 설정
        }

        log.info("mediaType: {}", mediaType);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(urlResource);
    }


}
