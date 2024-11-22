package com.spacedog.order.impl;

import com.spacedog.order.domain.Order;
import com.spacedog.order.dto.OrderDetailResponse;
import com.spacedog.order.repository.OrderRepository;
import com.spacedog.order.service.OrderException;
import com.spacedog.order.service.OrderItemResponse;
import com.spacedog.order.service.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderFinder {

    private final OrderRepository orderRepository;

    public OrderFinder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order find(Long orderId) {
        return orderRepository.findById(orderId);
    }


    public List<OrderResponse> findOrders(Long memberId) {

        // 루트 조회
        List<OrderResponse> orders = orderRepository.findOrders(memberId);

        List<Long> orderIds = orders.stream()
                .map(order -> order.getOrderId())
                .collect(Collectors.toList());

        Map<Long, List<OrderItemResponse>> orderItemsMap = orderRepository.findOrderItems(orderIds);


        orders.forEach(orderResponse -> {
            orderResponse.setOrderItems(orderItemsMap.get(orderResponse.getOrderId()));
        });

        return orders;
    }

    public OrderDetailResponse findDetailOrder(Long orderId) {
        return orderRepository.findOrderDetail(orderId);
    }

    public void orderValidator(Long itemId, Long memberId) {
        boolean result = orderRepository.existsByMemberIdAndItemId(itemId, memberId);

        if(!result) {
            throw new OrderException("상품을 구매한 사용자만 리뷰 작성이 가능합니다");
        }
    }


}
