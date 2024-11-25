package com.spacedog.review.service;

import com.spacedog.review.domain.ReviewReply;
import com.spacedog.review.repository.ReviewReplyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewReplyReader {


    private final ReviewReplyJpaRepository reviewRepository;

    public List<ReviewReply> findReviewReply(Long reviewId) {

        List<ReviewReply> reviewReplyList = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다"));

        return reviewReplyList;

    }
}
