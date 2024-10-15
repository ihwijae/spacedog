package com.spacedog.order.repository;

import com.spacedog.order.domain.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  OrderItemRepository extends JpaRepository<OrderItems, Long> {
}
