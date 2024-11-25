package com.spacedog.file.controller;

import com.spacedog.file.domain.ItemImage;
import com.spacedog.file.domain.ReviewImage;
import com.spacedog.file.service.ImageService;
import com.spacedog.global.ApiResponse;
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


    private final ImageService imageService;


    @PostMapping("/itemFiles/{itemId}")
    public ApiResponse<List<String>> uploadFile(@PathVariable("itemId") Long itemId, @RequestPart("multipartFiles") List<MultipartFile> multipartFiles) {
        log.info("파일 업로드 컨트롤러");
        List<ItemImage> fileData = imageService.uploadItemFiles(itemId, multipartFiles);


        List<String> resultPath = fileData.stream()
                .map(f -> f.getFilePath())
                .collect(Collectors.toList());

        return ApiResponse.success(resultPath, "파일 업로드 완료");
    }


    @GetMapping("/itemFiles/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("filename") String filename) throws IOException {
        log.info("filename: {}", filename);
        UrlResource urlResource = new UrlResource("file:" + imageService.getItemImageFullPath(filename));

        String contentType =
                Files.probeContentType(Paths.get(imageService.getItemImageFullPath(filename)));

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

    @GetMapping("/items/{itemId}/images")
    public ApiResponse<List<String>> getImages(@PathVariable("itemId") Long itemId) {
        List<String> filePath = imageService.getItemFile(itemId);

        return ApiResponse.success(filePath, "이미지 조회");
    }


    @PostMapping("/reviewFiles/{reviewId}")
    public ApiResponse<List<String>> uploadReviewFile(@PathVariable Long reviewId, @RequestPart List<MultipartFile> multipartFiles) {
        log.info("리뷰 이미지 파일 컨트롤러");
        List<ReviewImage> fileData = imageService.uploadReviewFiles(reviewId, multipartFiles);


        List<String> resultPath = fileData.stream()
                .map(f -> f.getFilePath())
                .collect(Collectors.toList());

        return ApiResponse.success(resultPath, "파일 업로드 완료");
    }



    @GetMapping("/review/{reviewId}/images")
    public ApiResponse<List<String>> getReviewImage(@PathVariable Long reviewId) {
        List<String> filePath = imageService.getReviewFile(reviewId);

        return ApiResponse.success(filePath, "이미지 조회");
    }

}
