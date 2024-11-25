package com.spacedog.review.repository;

import com.spacedog.file.domain.ReviewImage;
import com.spacedog.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {


}
