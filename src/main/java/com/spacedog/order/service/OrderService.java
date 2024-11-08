package com.spacedog.order.service;

import com.spacedog.delivery.impl.DeliveryWriter;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberReader;
import com.spacedog.order.domain.Order;
import com.spacedog.order.impl.OrderCreateRequest;
import com.spacedog.order.impl.OrderNumberGenerator;
import com.spacedog.order.impl.OrderWriter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {


    private final MemberReader memberReader;
    private final DeliveryWriter deliveryWriter;
    private final OrderWriter orderWriter;
    private final OrderNumberGenerator orderNumberGenerator;



    // 주문 생성
    @Transactional
    public Long createOrder(OrderCreateRequest request) {

        Member member = memberReader.getMember();

        // 배송지
        Long deliveryId = deliveryWriter.createDelivery(request.getAddress());


        String orderNumber = orderNumberGenerator.OrderNumberCreate();
        Order order =  orderWriter.createOrder(request, member.getId(), deliveryId, orderNumber);


        orderWriter.createOrderItems(request, member.getId(), order);


        return order.getId();
    }

    // 주문 조회
    

}
