package com.spacedog.review.service;

import com.spacedog.review.domain.ReviewReply;
import com.spacedog.review.dto.ReviewReplyCreateRequest;
import com.spacedog.review.repository.ReviewReplyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReviewReplyManager {

    private final ReviewReplyJpaRepository replyRepository;


    public Long createReviewReply(Long reviewId, Long memberId, ReviewReplyCreateRequest request) {

        ReviewReply reviewReply = ReviewReply.builder()
                .reviewId(reviewId)
                .comment(request.getComment())
                .memberId(memberId)
                .build();

        ReviewReply save = replyRepository.save(reviewReply);

        return save.getId();

    }
}
