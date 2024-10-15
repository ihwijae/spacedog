package com.spacedog.cart.domain;

import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    private Long itemId;

    private Long cartId;

    private int quantity;


}
