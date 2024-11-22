package com.spacedog.review.repository;

import com.spacedog.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageJpaRepository extends JpaRepository<ReviewImage, Long> {
}
