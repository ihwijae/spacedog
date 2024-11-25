package com.spacedog.review.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.item.domain.QItem;
import com.spacedog.member.domain.QMember;
import com.spacedog.review.domain.QReview;
import com.spacedog.review.domain.QReviewReply;
import com.spacedog.review.dto.ReviewReplyResponse;
import com.spacedog.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spacedog.item.domain.QItem.item;
import static com.spacedog.member.domain.QMember.member;
import static com.spacedog.review.domain.QReview.review;
import static com.spacedog.review.domain.QReviewReply.reviewReply;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;


    public boolean existReviewWithMember(Long memberId, Long reviewId) {

        Integer result = queryFactory
                .selectOne()
                .from(review)
                .where(review.memberId.eq(memberId).and(review.id.eq(reviewId)))
                .fetchFirst();

        return result != null;

    }

    public List<ReviewResponse> findReviewAll(Long itemId, int pageNo, int pageSize) {

        List<Long> ids = queryFactory.select(review.id)
                .from(review)
                .where(review.itemId.eq(itemId))
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }


        List<ReviewResponse> reviewResponses = queryFactory.select(Projections.fields(ReviewResponse.class,
                        review.id.as("reviewId"),
                        member.id.as("memberId"),
                        member.name.as("memberName"),
                        review.itemId.as("itemId"),
                        review.content.as("content"),
                        review.rating.as("rating")
                ))
                .from(review)
                .join(member).on(review.memberId.eq(member.id))
                .where(review.id.in(ids))
                .fetch();

        return reviewResponses;
    }

    public Map<Long, List<ReviewReplyResponse>> findReviewReply(List<Long> reviewIds) {

        List<ReviewReplyResponse> replyResponseList = queryFactory
                .select(Projections.fields(ReviewReplyResponse.class,
                        reviewReply.reviewId.as("reviewId"),
                        reviewReply.comment.as("comment")))
                .from(reviewReply)
                .where(reviewReply.reviewId.in(reviewIds))
                .fetch();

        Map<Long, List<ReviewReplyResponse>> result = replyResponseList.stream()
                .collect(Collectors.groupingBy(reviewReplyResponse -> reviewReplyResponse.getReviewId()));

        return result;

    }

}
