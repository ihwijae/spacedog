package com.spacedog.review.service;

import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberReader;
import com.spacedog.order.impl.OrderFinder;
import com.spacedog.review.domain.Review;
import com.spacedog.review.dto.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderFinder orderFinder;
    private final MemberReader memberReader;
    private final ReviewManager reviewManager;

    // 리뷰 등록
    public Long createReview(Long itemId, ReviewCreateRequest request) {

        Member member = memberReader.getMember();

        orderFinder.orderValidator(itemId, member.getId());

        reviewManager.createReview(member.getId(), itemId, request);

    }
}
