package com.spacedog.review.dto;

import lombok.Data;

@Data
public class ReviewEditRequest {


    private String content;
    private int rating;
}
