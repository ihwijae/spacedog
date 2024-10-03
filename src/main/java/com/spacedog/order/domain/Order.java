package com.spacedog.order.domain;

import com.spacedog.member.domain.BaseTimeEntity;
import jakarta.persistence.*;
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
   @Column(name = "member_id")
    private Long memberId;


    // 간접 참조
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "order_time")
    private LocalDateTime orderDate;


    @Column(name = "delivery_id")
    private Long deliveryId;



    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus orderStatus;


    /**
     *
     * 추후 수정 예정
     */
//    @OneToMany
//    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    private List<OrderItems> orderItems = new ArrayList<>();





}
