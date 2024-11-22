package com.spacedog.review.repository;

import com.spacedog.review.domain.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReplyJpaRepository extends JpaRepository<ReviewReply, Long> {
}
