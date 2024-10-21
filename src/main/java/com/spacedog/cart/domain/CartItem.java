package com.spacedog.cart.domain;

import com.spacedog.cart.dto.CartAddRequest;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.CreateItemRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    private Long itemId;

    private String itemName;

    private Long cartId;

    private int quantity;

    @ElementCollection
    @CollectionTable(name = "cart_item_option_specs_ids", joinColumns = @JoinColumn(name = "cart_item_id"))
    @Column(name = "option_specs_id") //cart_item_option_specs_ids 테이블에서 optionSpecsIds 지정할 컬럼 이름
    private List<Long> optionSpecsIds = new ArrayList<>();


    @Builder
    public CartItem(Long id, Long itemId, Long cartId, int quantity, Long optionSpecsIds, String itemName) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.cartId = cartId;
        this.quantity = quantity;
        this.optionSpecsIds.add(optionSpecsIds);
    }


    public CartItem() {
    }

    public static CartItem createItem(CartAddRequest cartAddRequest, Item item) {
        return CartItem.builder()
                .itemId(cartAddRequest.getItemId())
                .cartId(cartAddRequest.getCartId())
                .quantity(cartAddRequest.getQuantity())
                .itemName(item.getName())
                .build();
    }

    public void addOptionSpecsId(Long optionSpecsId) {
        this.optionSpecsIds.add(optionSpecsId);
    }



    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }


}
