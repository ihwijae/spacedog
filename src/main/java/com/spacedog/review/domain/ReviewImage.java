package com.spacedog.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "review_image")
@Getter
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String uploadFileName;
    private String storeFileName;
    private String filePath;

    private Long reviewId;

    @Builder
    public ReviewImage(long id, String uploadFileName, String storeFileName, String filePath, Long reviewId) {
        this.id = id;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.filePath = filePath;
        this.reviewId = reviewId;
    }

    public ReviewImage() {
    }


}
