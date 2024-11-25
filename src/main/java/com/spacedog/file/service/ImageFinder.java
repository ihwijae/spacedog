package com.spacedog.file.service;

import com.spacedog.file.domain.ItemImage;
import com.spacedog.file.domain.ReviewImage;
import com.spacedog.file.repository.FileRepository;
import com.spacedog.file.repository.ReviewImageJpaRepository;
import com.spacedog.review.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageFinder {

    private final FileRepository fileRepository;
    private final ReviewImageJpaRepository reviewImageJpaRepository;

    @Value("${itemImage.dir}")
    private String itemImageDir;

    @Value("${review.der")
    private String reviewImageDir;


    public String getItemImageFullPath(String fileName) {
        log.info("fileDir: {}", itemImageDir);
        return itemImageDir + "/" + fileName;
    }

    public String getReviewImageFullPath(String fileName) {
        return reviewImageDir + "/" + fileName;
    }


    public List<String> getItemImageFile(Long itemId) {
        List<ItemImage> fileData = fileRepository.findByItemId(itemId);

        List<String> result = fileData.stream()
                .map(f -> f.getFilePath())
                .collect(Collectors.toList());

        return result;
    }

    public List<String> getReviewImageFile(Long reviewId) {
        List<ReviewImage> fileData = reviewImageJpaRepository.findByReviewId(reviewId);

        List<String> result = fileData.stream()
                .map(f -> f.getFilePath())
                .collect(Collectors.toList());

        return result;
    }

}
