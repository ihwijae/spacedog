package com.spacedog.order.repository;

import com.spacedog.order.domain.OrderItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, Long> {
}
