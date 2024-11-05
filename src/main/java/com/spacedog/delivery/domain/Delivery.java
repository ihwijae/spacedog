package com.spacedog.delivery.domain;

import com.spacedog.member.domain.Address;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "delivery")
@Getter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeliveryStatus status;


    @Builder
    public Delivery(Long id, Address address, DeliveryStatus status) {
        this.id = id;
        this.address = address;
        this.status = status;
    }

    public Delivery() {
    }



}
