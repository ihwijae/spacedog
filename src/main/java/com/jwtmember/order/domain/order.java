//package com.jwtmember.order.domain;
//
//import com.jwtmember.member.domain.Member;
//import jakarta.persistence.*;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Getter
//@Table(name = "orders")
//public class order {
//
//
//    @Id
//    @GeneratedValue
//    @Column(name = "order_id")
//    private Long id;
//
//
//
//
//    // 객체 참조 -> 간접 참조로 변경 예정
//   @Column(name = "member_id")
//    private Long memberId;
//
//
//
//    // 간접 참조
//    @Column(name = "delivery_id")
//    private Long deliveryId;
//    private LocalDateTime orderTime;
//
//
//
//    @Enumerated(EnumType.STRING)
//    private OrderStatus orderStatus;
//
//
//
//
//
//}
