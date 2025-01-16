package com.spacedog.review.service;

import com.spacedog.review.domain.Review;
import com.spacedog.review.dto.ReviewReplyResponse;
import com.spacedog.review.dto.ReviewResponse;
import com.spacedog.review.exception.ReviewException;
import com.spacedog.review.repository.ReviewJpaRepository;
import com.spacedog.review.repository.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewReader {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewQueryRepository reviewQueryRepository;




    public Review findReview(Long reviewId) {

        return reviewJpaRepository.findById(reviewId)
                .orElseThrow( () -> new ReviewException("Review not found"));
    }

    public List<ReviewResponse> findReviewAll(Long itemId, int pageNo, int pageSize) {

        List<ReviewResponse> reviewAll = reviewQueryRepository.findReviewAll(itemId, pageNo, pageSize);

        List<Long> reviewIds = reviewAll.stream()
                .map(ReviewResponse::getReviewId)
                .collect(Collectors.toList());


        Map<Long, List<ReviewReplyResponse>> reviewReply = reviewQueryRepository.findReviewReply(reviewIds);


        reviewAll
                .forEach(reviewResponse ->  {
                    List<ReviewReplyResponse> reviewReplyResponses = reviewReply.get(reviewResponse.getReviewId());

                    reviewResponse.setReviewReplyResponses(reviewReplyResponses);
                });

        return reviewAll;
    }

}
