package com.spacedog.order.repository;

import com.spacedog.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaOrderRepository extends JpaRepository<Order, Long> {
}
