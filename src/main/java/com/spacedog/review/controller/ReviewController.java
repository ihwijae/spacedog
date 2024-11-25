package com.spacedog.review.controller;

import com.spacedog.global.ApiResponse;
import com.spacedog.review.dto.ReviewCreateRequest;
import com.spacedog.review.dto.ReviewEditRequest;
import com.spacedog.review.dto.ReviewReplyCreateRequest;
import com.spacedog.review.dto.ReviewResponse;
import com.spacedog.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {


    private final ReviewService reviewService;


    @GetMapping("/review/{itemId}")
    public ApiResponse<List<ReviewResponse>> findReviewAll(@PathVariable("itemId") Long itemId,
                                                           @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        List<ReviewResponse> reviewAll = reviewService.findReviewAll(itemId, pageNo, pageSize);

        return ApiResponse.success(reviewAll, "리뷰 전체 조회");
    }

    @PostMapping("/reviewReply/{reviewId}")
    public ApiResponse<Long> createReviewReply(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewReplyCreateRequest request) {

        log.info("reviewReplyName={}", request.getComment());

        Long reviewReply = reviewService.createReviewReply(reviewId, request);

        return ApiResponse.success(reviewReply, "리뷰 댓글 생성");
    }



    @PostMapping("/review/{itemId}")
    public ApiResponse<Long> createTemporaryReview(@PathVariable("itemId") Long itemId) {

        Long temporaryReview = reviewService.createTemporaryReview(itemId);

        return ApiResponse.success(temporaryReview, "리뷰 임시 생성");

    }

    @PatchMapping("/review/{reviewId}")
    public ApiResponse createReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewCreateRequest request) {

        reviewService.createReview(reviewId, request);

        return ApiResponse.successNoResponse("리뷰 저장");
        }



        @PatchMapping("/review/edit/{reviewId}")
    public ApiResponse editReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewEditRequest request) {

        reviewService.editReview(reviewId, request);

            return ApiResponse.successNoResponse("리뷰 수정");
        }

        @DeleteMapping("/review/{reviewId}")
    public ApiResponse deleteReview(@PathVariable("reviewId") Long reviewId) {

        reviewService.deleteReview(reviewId);

            return ApiResponse.successNoResponse("리뷰 삭제");
        }





}
