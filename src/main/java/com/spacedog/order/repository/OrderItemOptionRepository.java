package com.spacedog.order.repository;

import com.spacedog.order.domain.OrderItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, Long> {

    // orderItemId 리스트에 해당하는 optionId들을 조회
    List<Long> findByOrderItemIdIn(List<Long> orderItemIds);
    List<OrderItemOption> findByOrderItemId(Long orderItemId);
}
