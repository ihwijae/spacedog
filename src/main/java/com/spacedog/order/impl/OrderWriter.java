package com.spacedog.order.impl;

import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.exception.NotEnoughStockException.OptionSpecsNotFound;
import com.spacedog.item.impl.ItemReader;
import com.spacedog.member.domain.Member;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.spacedog.order.impl.OrderCreateRequest.*;

@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;



    public Long createOrder(OrderCreateRequest request, Member member, Long deliveryId, String orderNumber) {

        // 주문 생성
        Order order = Order.create(request, member, deliveryId, orderNumber);

        // 주문 저장
        Order saveOrder = orderRepository.save(order);


        return saveOrder.getId();
    }


    public void createOrderItems(List<OrderItemCreate> orderItemCreates,
                                   Order order,
                                   List<CartItem> cartItems,
                                   List<OptionSpecification> optionSpecifications) {

        List<OrderItems> orderItemsList = orderItemCreates.stream()
                .map(orderItem -> OrderItems.createOrderItem(
                        orderItem.getItemId(), order, orderItem.getOrderPrice(), orderItem.getAmount()))
                .collect(Collectors.toList());

        List<OrderItems> resultOrderItems = orderItemRepository.saveAll(orderItemsList);


        if(!optionSpecifications.isEmpty()) {

            // OptionSpecifications를 OrderItems와 매핑하여 OrderItemOption 생성
            List<OrderItemOption> orderItemOptions = OrderItemOption.create(resultOrderItems, optionSpecifications);
            orderItemOptionRepository.saveAll(orderItemOptions);

        }

        cartItemRepository.deleteAll(cartItems);

    }


}
