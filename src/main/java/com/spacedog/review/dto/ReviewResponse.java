package com.spacedog.review.dto;

import com.spacedog.review.domain.ReviewReply;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewResponse {


    private Long reviewId;
    private Long memberId;
    private String memberName;
    private Long itemId;
    private String content;
    private int rating;
    private List<ReviewReplyResponse> reviewReplyResponses = new ArrayList<>();

    public ReviewResponse(Long reviewId, Long memberId, String memberName, Long itemId, String content, int rating, List<ReviewReplyResponse> reviewReplyResponses) {
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.itemId = itemId;
        this.content = content;
        this.rating = rating;
        this.reviewReplyResponses = reviewReplyResponses;
    }

    public ReviewResponse() {
    }
}
