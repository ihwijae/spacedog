package com.spacedog.file.service;

import com.spacedog.file.domain.ItemImage;
import com.spacedog.file.domain.ReviewImage;
import com.spacedog.file.ex.FileException;
import com.spacedog.file.repository.FileRepository;
import com.spacedog.file.repository.ReviewImageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UploadManager {

    private final FileRepository fileRepository;
    private final ReviewImageJpaRepository reviewImageJpaRepository;
    private final ImageFinder imageFinder;


    public List<ItemImage> uploadItemFiles(Long itemId, List<MultipartFile> multipartFiles) {
        List<ItemImage> files = new ArrayList<>();

        multipartFiles.forEach(f -> {
            if(!multipartFiles.isEmpty()) {
                ItemImage itemImage = uploadItemFile(itemId, f);
                files.add(itemImage);
            }
        });
        return files;
    }

    public ItemImage uploadItemFile(Long itemId, MultipartFile multipartFile) {
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
            multipartFile.transferTo(new File(imageFinder.getItemImageFullPath(storeFileName)));
        } catch (IOException e) {
            throw new FileException("파일을 업로드 할 수 없습니다", e);
        }

        ItemImage itemImage = ItemImage.builder()
                .uploadFilename(originalFilename)
                .storeFilename(storeFileName)
                .filePath(imageFinder.getItemImageFullPath(storeFileName))
                .itemId(itemId)
                .build();

        return fileRepository.save(itemImage);

    }


    public List<ReviewImage> uploadReviewFiles(Long reviewId, List<MultipartFile> multipartFiles) {
        List<ReviewImage> files = new ArrayList<>();

        multipartFiles.forEach(f -> {
            if(!multipartFiles.isEmpty()) {
                ReviewImage reviewImage = uploadReviewFile(reviewId, f);
                files.add(reviewImage);
            }
        });
        return files;
    }

    public ReviewImage uploadReviewFile(Long reviewId, MultipartFile multipartFile) {
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
            multipartFile.transferTo(new File(imageFinder.getItemImageFullPath(storeFileName)));
        } catch (IOException e) {
            throw new FileException("파일을 업로드 할 수 없습니다", e);
        }

        ReviewImage reviewImage = ReviewImage
                .builder()
                .uploadFileName(originalFilename)
                .storeFileName(storeFileName)
                .filePath(imageFinder.getReviewImageFullPath(storeFileName))
                .reviewId(reviewId)
                .build();


        return reviewImageJpaRepository.save(reviewImage);

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
