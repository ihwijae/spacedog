package com.spacedog.review.service;

import com.spacedog.review.domain.Review;
import com.spacedog.review.domain.ReviewReply;
import com.spacedog.review.dto.ReviewReplyCreateRequest;
import com.spacedog.review.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReviewManager {

    private final ReviewJpaRepository reviewJpaRepository;

    public Long createTemporaryReview(Long itemId, Long memberId) {
        Review temporaryReview = Review.createTemporaryReview(itemId, memberId);
        Review result = reviewJpaRepository.save(temporaryReview);
        return result.getId();
    }




    public void deleteReview(Review review) {

        reviewJpaRepository.delete(review);
    }






}
