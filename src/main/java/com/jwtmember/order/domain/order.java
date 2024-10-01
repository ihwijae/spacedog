package com.jwtmember.order.domain;

import com.jwtmember.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "orders")
public class order {


    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;




    // 객체 참조 -> 간접 참조로 변경 예정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;



    // 간접 참조
    private Long deliveryId;
    private LocalDateTime orderTime;



    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;





}
