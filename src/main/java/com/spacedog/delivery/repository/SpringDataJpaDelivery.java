package com.spacedog.delivery.repository;

import com.spacedog.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaDelivery extends JpaRepository<Delivery, Long> {
}
