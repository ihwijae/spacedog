package com.spacedog.order.service;

import com.spacedog.cart.domain.CartItem;
import com.spacedog.delivery.impl.DeliveryWriter;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberReader;
import com.spacedog.option.domain.OptionSpecification;
import com.spacedog.order.domain.Order;
import com.spacedog.order.domain.OrderItemOption;
import com.spacedog.order.dto.OrderDetailResponse;
import com.spacedog.order.impl.OrderCreateRequest;
import com.spacedog.order.impl.OrderNumberGenerator;
import com.spacedog.order.impl.OrderFinder;
import com.spacedog.order.impl.OrderWriter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.spacedog.order.impl.OrderCreateRequest.*;


@Service
@RequiredArgsConstructor
public class OrderService {


    private final MemberReader memberReader;
    private final DeliveryWriter deliveryWriter;
    private final OrderWriter orderWriter;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderFinder orderFinder;
    private final StockManager stockManager;
    private final CartItemReader cartItemReader;
    private final OptionReader optionReader;




    // 주문 생성
    @Transactional
    public Long createOrder(OrderCreateRequest request) {

        List<OrderItemCreate> orderItemCreate = request.getOrderItemCreate();

        Member member = memberReader.getMember();

        // 배송지
        Long deliveryId = deliveryWriter.createDelivery(request.getAddress());


        // 재고 확인
        stockManager.checkQuantity(request);
        stockManager.stockDecrease(request);


        // 주문 번호 생성
        String orderNumber = orderNumberGenerator.OrderNumberCreate();

        Long orderId = orderWriter.createOrder(request, member, deliveryId, orderNumber);

        List<CartItem> cartItem = cartItemReader.getCartItem(orderItemCreate, member.getId());
        List<OptionSpecification> optionSpecs = optionReader.findOptionSpecs(orderItemCreate);


        Order order = orderFinder.find(orderId);

        orderWriter.createOrderItems(orderItemCreate, order, cartItem, optionSpecs);


        return order.getId();
    }

    //전체 주문 조회
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders() {
        Member member = memberReader.getMember();

        return orderFinder.findOrders(member.getId());

    }

    //주문 상세 조회
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long orderId) {

        return orderFinder.findDetailOrder(orderId);
    }

}
