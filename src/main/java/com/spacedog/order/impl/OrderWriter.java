package com.spacedog.order.impl;

import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.impl.ItemReader;
import com.spacedog.option.domain.OptionSpecification;
import com.spacedog.option.repository.OptionSpecsRepository;
import com.spacedog.order.domain.Order;
import com.spacedog.order.domain.OrderItems;
import com.spacedog.order.repository.OrderItemRepository;
import com.spacedog.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemReader itemReader;
    private final OptionSpecsRepository optionSpecsRepository;
    private final CartItemRepository cartItemRepository;



    public Order createOrder(OrderCreateRequest request, Long memberId, Long deliveryId) {

        // 주문 생성
        Order order = Order.create(request, memberId, deliveryId);

        // 주문 저장
        Order saveOrder = orderRepository.save(order);

        return saveOrder;
    }


    public void createOrderItems(OrderCreateRequest request, Long memberId, Order order) {

        request.getOrderItemCreate().forEach(orderItemCreate -> {


            Item item = itemReader.findById(orderItemCreate.getItemId());

            // 주문시 재고 처리
            if(orderItemCreate.getOptionId() != null) {
                OptionSpecification optionSpec = optionSpecsRepository.findById(orderItemCreate.getOptionId())
                        .orElseThrow(() -> new NotEnoughStockException.OptionSpecsNotFound("option specs not found"));

                optionSpec.removeQuantity(orderItemCreate.getAmount(), item);
            } else {
                item.removeStock(orderItemCreate.getAmount());
            }


            OrderItems orderItem = OrderItems.createOrderItem(
                    orderItemCreate.getItemId(), order, orderItemCreate.getOrderPrice(), orderItemCreate.getAmount());
            orderItemRepository.save(orderItem);

            CartItem cartItem = cartItemRepository.findByItemIdWithMember(
                            orderItem.getItemId(), memberId)
                    .orElseThrow(() -> new NotEnoughStockException("장바구니에 상품이 존재하지 않습니다"));
            cartItemRepository.delete(cartItem);
        });
    }
}
