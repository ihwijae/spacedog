package com.spacedog.order.service;

import com.spacedog.delivery.impl.DeliveryWriter;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberReader;
import com.spacedog.order.domain.Order;
import com.spacedog.order.impl.OrderCreateRequest;
import com.spacedog.order.impl.OrderNumberGenerator;
import com.spacedog.order.impl.OrderFinder;
import com.spacedog.order.impl.OrderWriter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {


    private final MemberReader memberReader;
    private final DeliveryWriter deliveryWriter;
    private final OrderWriter orderWriter;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderFinder orderFinder;
    private final StockManager stockManager;




    // 주문 생성
    @Transactional
    public Long createOrder(OrderCreateRequest request) {

        Member member = memberReader.getMember();

        // 배송지
        Long deliveryId = deliveryWriter.createDelivery(request.getAddress());


        // 재고 확인
        stockManager.checkQuantity(request);

        // 재고 처리



        // 주문 번호 생성
        String orderNumber = orderNumberGenerator.OrderNumberCreate();


        Order order =  orderWriter.createOrder(request, member.getId(), deliveryId, orderNumber);


        orderWriter.createOrderItems(request, member.getId(), order);
        orderWriter.createOrderItemOption();


        return order.getId();
    }

    //전체 주문 조회
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders() {
        Member member = memberReader.getMember();

        return orderFinder.findOrders(member.getId());

    }

    //주문 상세 조회
//    @Transactional(readOnly = true)
//    public OrderResponse getOrderDetail(Long orderId) {
//
//    }

}
