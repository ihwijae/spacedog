package com.spacedog.order.repository;

import com.spacedog.order.domain.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface  OrderItemRepository extends JpaRepository<OrderItems, Long> {

    Optional<List<OrderItems>> findByOrderId(Long orderId);

}
