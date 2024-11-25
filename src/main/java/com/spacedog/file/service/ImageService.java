package com.spacedog.file.service;

import com.spacedog.file.domain.ItemImage;
import com.spacedog.file.domain.ReviewImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageFinder imageFinder;
    private final UploadManager uploadManager;



    public String getItemImageFullPath(String fileName) {

        return imageFinder.getItemImageFullPath(fileName);

    }

    public List<ItemImage> uploadItemFiles(Long itemId, List<MultipartFile> multipartFiles) {

        return uploadManager.uploadItemFiles(itemId, multipartFiles);
    }

    public ItemImage uploadItemFile(Long itemId, MultipartFile multipartFile) {

        return uploadManager.uploadItemFile(itemId, multipartFile);

    }

    public List<String> getItemFile(Long itemId) {

       return imageFinder.getItemImageFile(itemId);
    }

    public String getReviewImageFullPath(String fileName) {

        return imageFinder.getReviewImageFullPath(fileName);
    }

    public List<ReviewImage> uploadReviewFiles(Long reviewId, List<MultipartFile> multipartFiles) {

        return uploadManager.uploadReviewFiles(reviewId, multipartFiles);
    }


    public ReviewImage uploadReviewFile(Long itemId, MultipartFile multipartFile) {

        return uploadManager.uploadReviewFile(itemId, multipartFile);

    }


    public List<String> getReviewFile(Long itemId) {

        return imageFinder.getReviewImageFile(itemId);
    }




}
