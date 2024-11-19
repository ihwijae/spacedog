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
import com.spacedog.order.domain.OrderItemOption;
import com.spacedog.order.domain.OrderItems;
import com.spacedog.order.repository.OrderItemOptionRepository;
import com.spacedog.order.repository.OrderItemRepository;
import com.spacedog.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;

@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OptionSpecsRepository optionSpecsRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;



    public Order createOrder(OrderCreateRequest request, Long memberId, Long deliveryId, String orderNumber) {

        // 주문 생성
        Order order = Order.create(request, memberId, deliveryId, orderNumber);

        // 주문 저장
        Order saveOrder = orderRepository.save(order);


        return saveOrder;
    }


    public void createOrderItems(OrderCreateRequest request, Long memberId, Order order) {

        request.getOrderItemCreate().forEach(orderItemCreate -> {

            OrderItems orderItem;
            OptionSpecification optionSpec = null;

            if(orderItemCreate.getOptionId() != null) {
                optionSpec = optionSpecsRepository.findById(orderItemCreate.getOptionId())
                        .orElseThrow(() -> new OptionSpecsNotFound("옵션을 찾을 수 없습니다"));

                orderItem = OrderItems.createOrderItem(
                        orderItemCreate.getItemId(), order, orderItemCreate.getOrderPrice(), orderItemCreate.getAmount(), optionSpec.getId());

            } else {

                orderItem = OrderItems.builder()
                        .itemId(orderItemCreate.getItemId())
                        .order(order)
                        .orderPrice(orderItemCreate.getOrderPrice())
                        .orderCount(orderItemCreate.getAmount())
                        .build();

            }

            OrderItems resultOrderItems = orderItemRepository.save(orderItem);
            OrderItemOption orderItemOption = OrderItemOption.create(orderItem.getId(), optionSpec);
            orderItemOptionRepository.save(orderItemOption);

            CartItem cartItem = cartItemRepository.findByItemIdWithMember(
                            resultOrderItems.getItemId(), memberId)
                    .orElseThrow(() -> new NotEnoughStockException("장바구니에 상품이 존재하지 않습니다"));
            cartItemRepository.delete(cartItem);
        });
    }

    public void createOrderItemOption() {


    }
}
