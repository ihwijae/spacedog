package com.spacedog.review.dto;

import lombok.Data;

@Data
public class ReviewReplyResponse {

    private Long reviewId;
    private String comment;

    public ReviewReplyResponse() {
    }

    public ReviewReplyResponse(Long reviewId, String comment) {
        this.reviewId = reviewId;
        this.comment = comment;
    }
}
