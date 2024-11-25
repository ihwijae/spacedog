package com.spacedog.review.service;

import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberReader;
import com.spacedog.order.impl.OrderFinder;
import com.spacedog.review.domain.Review;
import com.spacedog.review.domain.ReviewReply;
import com.spacedog.review.dto.ReviewCreateRequest;
import com.spacedog.review.dto.ReviewEditRequest;
import com.spacedog.review.dto.ReviewReplyCreateRequest;
import com.spacedog.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderFinder orderFinder;
    private final MemberReader memberReader;
    private final ReviewManager reviewManager;
    private final ReviewReader reviewReader;
    private final ReviewValidator reviewValidator;
    private final ReviewReplyManager reviewReplyManager;
    private final ReviewReplyReader reviewReplyReader;


    public List<ReviewResponse> findReviewAll(Long itemId, int pageNo, int pageSize) {

        List<ReviewResponse> reviewAll = reviewReader.findReviewAll(itemId, pageNo, pageSize);

        return reviewAll;
    }

    public Long createReviewReply(Long reviewId, ReviewReplyCreateRequest request) {

        Member member = memberReader.getMember();

        Long reviewReply = reviewReplyManager.createReviewReply(reviewId, member.getId(), request);

        return reviewReply;

    }



    // 리뷰 임시 생성
    public Long createTemporaryReview(Long itemId) {

        Member member = memberReader.getMember();

        orderFinder.orderValidator(itemId, member.getId());

        Long temporaryReviewId = reviewManager.createTemporaryReview(itemId, member.getId());

        return temporaryReviewId;
    }

    // 리뷰 등록
    @Transactional
    public void createReview(Long reviewId, ReviewCreateRequest request) {


        Review review = reviewReader.findReview(reviewId);
        review.createReview(request);


    }

    @Transactional
    public void editReview(Long reviewId, ReviewEditRequest request) {

        Member member = memberReader.getMember();

        reviewValidator.writeReviewMemberCheck(member.getId(), reviewId);

        Review review = reviewReader.findReview(reviewId);

        review.editReview(request);
    }


    public void deleteReview(Long reviewId) {

        Member member = memberReader.getMember();

        Review review = reviewReader.findReview(reviewId);

        reviewValidator.writeReviewMemberCheck(member.getId(), review.getId());


        List<ReviewReply> reviewReply = reviewReplyReader.findReviewReply(reviewId);

        reviewReplyManager.deleteReviewReply(reviewReply);
        reviewManager.deleteReview(review);

    }
}
