package com.spacedog.cart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "cart_item_option_specs_ids")
@Getter
public class CartOptionSpecs {

    @Id
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(name = "option_specs_id")
    private Long optionSpecsId;
}
