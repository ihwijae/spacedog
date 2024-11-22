package com.spacedog.review.domain;

import com.spacedog.member.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table (name = "review_reply")
@Getter
public class ReviewReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reviewId;

    private Long memberId;

    private String comment;

    @Builder
    public ReviewReply(Long id, Long reviewId, Long memberId, String comment) {
        this.id = id;
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.comment = comment;
    }

    public ReviewReply() {
    }
}
