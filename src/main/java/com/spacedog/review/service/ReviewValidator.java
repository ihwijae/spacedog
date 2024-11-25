package com.spacedog.review.service;

import com.spacedog.review.exception.ReviewException;
import com.spacedog.review.repository.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewValidator {

    private final ReviewQueryRepository reviewQueryRepository;

    public void writeReviewMemberCheck(Long memberId, Long reviewId) {

        boolean result = reviewQueryRepository.existReviewWithMember(memberId, reviewId);

        if(!result) {
            throw new ReviewException("리뷰를 작성한 회원이 아닙니다");
        }

    }
}
