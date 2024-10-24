package com.spacedog.cart.domain;

import com.spacedog.member.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Cart extends BaseTimeEntity {

    @Id
    @Column(name = "cart_id")
    private Long id;


    @Builder
    public Cart(Long id, int totalPrice, int totalItems) {
        this.id = id;
    }


    public Cart() {
    }
}
