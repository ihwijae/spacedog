package com.spacedog.order.domain;

import com.spacedog.generic.Money;
import com.spacedog.generic.MoneyConverter;
import com.spacedog.member.domain.Address;
import com.spacedog.member.domain.BaseTimeEntity;
import com.spacedog.member.domain.Member;
import com.spacedog.order.service.OrderCreateRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;


    // 라이프사이클이 다르기 때문에 객체참조 x -> 간접참조
   @Column(name = "customer_id")
    private Long customerId;



   @Column(name = "customer_name")
   private String name;

   @Column(name = "phone")
   private String phone;

    @Embedded
    private Address address;

    @Column(name = "requirement")
    private String requirement; // 요청사항


    @Column(name = "total_price")
//    @Convert(converter = MoneyConverter.class)
    private int totalPrice;

    @Column(name = "order_time")
    private LocalDateTime orderDate;


    @Column(name = "delivery_id")
    private Long deliveryId;



    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus orderStatus;



    public Order() {
    }

    @Builder
    public Order(Long id, Long customerId, String name, String phone, Address address, String requirement, int totalPrice, LocalDateTime orderDate, Long deliveryId, OrderStatus orderStatus) {
        this.id = id;
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.requirement = requirement;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.deliveryId = deliveryId;
        this.orderStatus = orderStatus;
    }


    // 주문 생성 메서드
    public static Order create(OrderCreateRequest request, Member member) {

//
//        int totalPrice = request.getOrderItemCreate()
//                .stream()
//                .map(orderItemCreate -> orderItemCreate.getOrderPrice())
//                .reduce(0, (price1, price2) -> price1 + price2);
        // price1 -> 이전까지의 누적 합계 또는 초기 값
        // price2 -> 현재 처리중인 요소의 값

        //참고로 배열의 요소는 forEach 람다 표현식안에 수정이 가능하지만
        // 지역변수는 람다 표현식안에서 재할당이 불가능함 (그래서 .stream(), .reduce() 메서드를 사용해야함

        int totalPrice = request.getOrderItemCreate()
                .stream()
                .mapToInt(orderItemCreate -> orderItemCreate.getOrderPrice() * orderItemCreate.getAmount())
                .sum();  // 총합을 계산

        // 누적된 totalPrice로 주문 생성
        return Order.builder()
                .customerId(member.getId())
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .orderDate(LocalDateTime.now())
                .totalPrice(totalPrice)  // 최종 금액 설정
                .orderStatus(OrderStatus.ORDER)
                .build();
    }


    /**
     *
     * 추후 수정 예정
     */
//    @OneToMany
//    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    private List<OrderItems> orderItems = new ArrayList<>();





}
