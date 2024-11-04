package com.spacedog.order.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.repository.ItemJpaRepository;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberService;
import com.spacedog.order.domain.Order;
import com.spacedog.order.domain.OrderItems;
import com.spacedog.order.repository.OrderItemRepository;
import com.spacedog.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberService memberService;
    private final ItemJpaRepository itemJpaRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    // 주문 생성
    public Long createOrder(OrderCreateRequest request) {

        Member member = memberService.getMember();

        // 주문 생성
        Order order = Order.create(request, member);

        // 주문 저장
        Order saveOrder = orderRepository.save(order);

        // 주문 상품 저장
        request.getOrderItemCreate().forEach(orderItemCreate -> {
            Item item = itemJpaRepository.findById(orderItemCreate.getItemId())
                    .orElseThrow(() -> new NotEnoughStockException.ItemNotFound("상품을 찾을 수 없습니다"));

            OrderItems orderItem = OrderItems.createOrderItem(orderItemCreate.getItemId(), order, orderItemCreate.getOrderPrice(), orderItemCreate.getAmount());
            orderItemRepository.save(orderItem);
        });

        return saveOrder.getId();
    }
}
