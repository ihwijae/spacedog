package com.spacedog.cart.domain;

import com.spacedog.member.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
public class Cart extends BaseTimeEntity {

    @Id
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "total_items")
    private int totalItems;


    @Builder
    public Cart(Long id, int totalPrice, int totalItems) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
    }


    public Cart() {

    }
}
