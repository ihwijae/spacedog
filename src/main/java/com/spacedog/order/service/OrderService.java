package com.spacedog.order.service;

import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.cart.repository.CartRepository;
import com.spacedog.delivery.domain.Delivery;
import com.spacedog.delivery.domain.DeliveryStatus;
import com.spacedog.delivery.repository.DeliveryRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.exception.NotEnoughStockException.ItemNotFound;
import com.spacedog.item.exception.NotEnoughStockException.OptionSpecsNotFound;
import com.spacedog.item.repository.ItemJpaRepository;
import com.spacedog.item.repository.ItemRepositoryPort;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberService;
import com.spacedog.option.domain.OptionGroupSpecification;
import com.spacedog.option.domain.OptionSpecification;
import com.spacedog.option.repository.OptionRepository;
import com.spacedog.option.repository.OptionSpecsRepository;
import com.spacedog.order.domain.Order;
import com.spacedog.order.domain.OrderItems;
import com.spacedog.order.repository.OrderItemRepository;
import com.spacedog.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepositoryPort itemRepository;
    private final OptionSpecsRepository optionSpecsRepository;

    // 주문 생성
    @Transactional
    public Long createOrder(OrderCreateRequest request, Member member) {


        // 배송지
        Delivery delivery = Delivery.builder()
                .address(request.getAddress())
                .status(DeliveryStatus.PENDING)
                .build();

        Delivery saveDelivery = deliveryRepository.save(delivery);


        // 주문 생성
        Order order = Order.create(request, member, saveDelivery.getId());

        // 주문 저장
        Order saveOrder = orderRepository.save(order);

        // 주문 상품 저장
        request.getOrderItemCreate().forEach(orderItemCreate -> {


            Item item = itemRepository.findById(orderItemCreate.getItemId())
                    .orElseThrow(() -> new ItemNotFound("item not found"));


            // 주문시 재고 처리
            if(orderItemCreate.getOptionId() != null) {
                OptionSpecification optionSpec = optionSpecsRepository.findById(orderItemCreate.getOptionId())
                        .orElseThrow(() -> new OptionSpecsNotFound("option specs not found"));

                optionSpec.removeQuantity(orderItemCreate.getAmount(), item);
            } else {
                item.removeStock(orderItemCreate.getAmount());
            }


            OrderItems orderItem = OrderItems.createOrderItem(
                    orderItemCreate.getItemId(), order, orderItemCreate.getOrderPrice(), orderItemCreate.getAmount());
            orderItemRepository.save(orderItem);

            CartItem cartItem = cartItemRepository.findByItemIdWithMember(
                    orderItem.getItemId(), member.getId())
                    .orElseThrow(() -> new NotEnoughStockException("장바구니에 상품이 존재하지 않습니다"));
            cartItemRepository.delete(cartItem);
        });


        return saveOrder.getId();
    }
}
