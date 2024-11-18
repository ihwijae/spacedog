package com.spacedog.order.impl;

import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.exception.NotEnoughStockException.OptionSpecsNotFound;
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



    public Order createOrder(OrderCreateRequest request, Long memberId, Long deliveryId, String orderNumber) {

        // 주문 생성
        Order order = Order.create(request, memberId, deliveryId, orderNumber);

        // 주문 저장
        Order saveOrder = orderRepository.save(order);

        return saveOrder;
    }


    public void createOrderItems(OrderCreateRequest request, Long memberId, Order order) {

        request.getOrderItemCreate().forEach(orderItemCreate -> {


            Item item = itemReader.findById(orderItemCreate.getItemId());


            //옵션 추출
            OptionSpecification optionSpec = optionSpecsRepository.findById(orderItemCreate.getOptionId())
                    .orElseThrow(() -> new OptionSpecsNotFound("option specs not found"));


            // 주문시 재고 처리
            if(orderItemCreate.getOptionId() != null) {

                optionSpec.removeQuantity(orderItemCreate.getAmount(), item);

            } else {
                item.removeStock(orderItemCreate.getAmount());
            }

            OrderItems orderItem = OrderItems.createOrderItem(
                    orderItemCreate.getItemId(), order, orderItemCreate.getOrderPrice(), orderItemCreate.getAmount(), optionSpec.getId());
            orderItemRepository.save(orderItem);

            CartItem cartItem = cartItemRepository.findByItemIdWithMember(
                            orderItem.getItemId(), memberId)
                    .orElseThrow(() -> new NotEnoughStockException("장바구니에 상품이 존재하지 않습니다"));
            cartItemRepository.delete(cartItem);
        });
    }
}
