package com.spacedog.review.service;

import com.spacedog.review.domain.Review;
import com.spacedog.review.dto.ReviewCreateRequest;
import com.spacedog.review.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewManager {

    private final ReviewJpaRepository reviewJpaRepository;


    public Long createReview(Long memberId, Long itemId, ReviewCreateRequest request) {

        Review review = Review.createReview(memberId, itemId, request);

        Review saveReview = reviewJpaRepository.save(review);

        return saveReview.getId();
    }
}
