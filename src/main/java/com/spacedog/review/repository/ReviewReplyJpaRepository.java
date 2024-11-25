package com.spacedog.review.repository;

import com.spacedog.review.domain.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewReplyJpaRepository extends JpaRepository<ReviewReply, Long> {

    Optional<List<ReviewReply>> findByReviewId(Long id);
}
