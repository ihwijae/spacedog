package com.spacedog.review.dto;

import lombok.Data;

@Data
public class ReviewCreateRequest {

    private String comment;
    private int rating;


    public ReviewCreateRequest(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public ReviewCreateRequest() {
    }
}
