package com.spacedog.order.service;

import com.spacedog.member.domain.Address;
import com.spacedog.member.domain.Member;
import com.spacedog.order.domain.Order;
import com.spacedog.order.impl.OrderCreateRequest;
import com.spacedog.order.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@SpringBootTest
class OrderServiceTest {


    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;



    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        TestingAuthenticationToken mockAuthentication = new TestingAuthenticationToken("lhj@gmail.com", "12345678");
        context.setAuthentication(mockAuthentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("주문 등록 테스트")
    void createOrder() {

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .name("테스트 주문")
                .phone("010-1234-5678")
                .address(new Address("안산시", "상록구", "본오동"))
                .orderItemCreate(createOrderItems())
                .build();

        Member member = Member.builder()
                .id(1L)
                .email("lhj@gmail.com")
                .build();


        Long orderId = orderService.createOrder(orderCreateRequest);
        Order order = orderRepository.findById(orderId).orElseThrow();


        Assertions.assertThat(orderId).isEqualTo(order.getId());
    }

    private List<OrderCreateRequest.OrderItemCreate> createOrderItems() {
        OrderCreateRequest.OrderItemCreate build1 = OrderCreateRequest.OrderItemCreate.builder()
                .itemId(1L)
                .amount(1)
                .orderPrice(10000)
                .build();

        OrderCreateRequest.OrderItemCreate build2=  OrderCreateRequest.OrderItemCreate.builder()
                .itemId(2L)
                .amount(3)
                .orderPrice(30000)
                .build();

        return List.of(build1, build2);
    }

}