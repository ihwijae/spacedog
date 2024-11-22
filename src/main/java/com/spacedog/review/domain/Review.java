package com.spacedog.review.domain;

import com.spacedog.member.domain.BaseTimeEntity;
import com.spacedog.review.dto.ReviewCreateRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table (name = "review")
@Getter
public class Review extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long memberId;

    private Long itemId;

    private String content;

    private int rating;


    @Builder
    public Review(Long id, Long memberId, Long itemId, String content, int rating) {
        this.id = id;
        this.memberId = memberId;
        this.itemId = itemId;
        this.content = content;
        this.rating = rating;
    }

    public Review() {
    }

    public static Review createReview(Long memberId, Long itemId,ReviewCreateRequest request) {

        return Review.builder()
                .memberId(memberId)
                .itemId(itemId)
                .content(request.getComment())
                .rating(request.getRating())
                .build();
    }
}
