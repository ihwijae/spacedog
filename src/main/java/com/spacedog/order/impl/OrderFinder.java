package com.spacedog.order.impl;

import com.spacedog.order.domain.Order;
import com.spacedog.order.domain.OrderItemOption;
import com.spacedog.order.domain.OrderItems;
import com.spacedog.order.domain.OrderStatus;
import com.spacedog.order.dto.OrderDetailResponse;
import com.spacedog.order.repository.OrderItemOptionRepository;
import com.spacedog.order.repository.OrderItemRepository;
import com.spacedog.order.repository.OrderRepository;
import com.spacedog.order.service.OrderException;
import com.spacedog.order.service.OrderItemResponse;
import com.spacedog.order.service.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderFinder {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;


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

        log.info("itemId={}, memberId={}", itemId, memberId);

        boolean result = orderRepository.existsByMemberIdAndItemId(itemId, memberId);

        log.info("result={}", String.valueOf(result));

        if(!result) {
            throw new OrderException("상품을 구매한 사용자만 리뷰 작성이 가능합니다");
        }
    }

    public Order orderCancleCheck(Long orderId) {

        Order order = orderRepository.findById(orderId);

       if(order.getOrderStatus() != OrderStatus.PENDING || order.getOrderStatus() != OrderStatus.COMPLETED) {
           throw new OrderException("주문 취소는 배송 시작전에 가능합니다 고객센터에 문의 하세요.");
       }

       return order;

    }

    public List<OrderItems> findOrderItems(Long orderId) {


        List<OrderItems> orderItems = orderItemRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderException("no orderItems"));

        return orderItems;

    }

//    public List<Long> findOrderItemOptions(List<OrderItems> orderItems) {
//
//
//        List<Long> orderItemIds = orderItems.stream()
//                .map(orderItem -> orderItem.getId())
//                .collect(Collectors.toList());
//
//
//        List<Long> orderItemsOptionIds = orderItemOptionRepository.findByOrderItemIdIn(orderItemIds);
//
//        if(orderItemsOptionIds.isEmpty() || orderItemIds == null) {
//            return Collections.emptyList();
//        }
//
//        return orderItemsOptionIds;
//
//
//    }

    public List<OrderItemOption> findOrderItemOptions(Long orderItemId) {


        List<OrderItemOption> orderItemOption = orderItemOptionRepository.findByOrderItemId(orderItemId);

        if(orderItemOption.isEmpty()) {
            return Collections.emptyList();
        }

        return orderItemOption;



    }


}
