package com.spacedog.order.repository;

import com.spacedog.order.domain.Order;
import com.spacedog.order.dto.OrderDetailResponse;
import com.spacedog.order.service.OrderItemResponse;
import com.spacedog.order.service.OrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderRepository {

    Order findById(long id);
    Order save(Order order);

    List<OrderResponse> findOrders(Long id);
    Map<Long, List<OrderItemResponse>> findOrderItems(List<Long> orderIds);

    OrderDetailResponse findOrderDetail(long orderId);


}
