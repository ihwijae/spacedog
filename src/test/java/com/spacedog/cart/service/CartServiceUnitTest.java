package com.spacedog.cart.service;

import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.dto.CartAddRequest;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.repository.ItemJpaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CartServiceUnitTest {


    private CartService cartService;

    @Test
    void 옵션이있는_상품을_장바구니에_담는다 () {

        //given
        Item item = Item.builder()
                .id(1L)
                .name("testItem")
                .build();

        CartAddRequest cartAddRequest = CartAddRequest.builder()
                .itemId(item.getId())
                .cartId(1L)
                .quantity(200)
                .optionSpecsIds(List.of(1L, 2L))
                .build();

        ItemJpaRepository itemJpaRepository = mock(ItemJpaRepository.class);
        CartItemRepository cartItemRepository = mock(CartItemRepository.class);
        cartService = new CartService(itemJpaRepository, cartItemRepository);

        when(itemJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(cartItemRepository.existByItemWithOptions(anyLong(), anyList())).thenReturn(false);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(CartItem.builder().id(1L).build());


        //when
        Long saveId = cartService.cartAddItems(cartAddRequest);

        //then
        assertThat(saveId).isEqualTo(1L);
        verify(cartItemRepository).existByItemWithOptions(cartAddRequest.getItemId(), cartAddRequest.getOptionSpecsIds());

    }

    @Test
    void 옵션이_없는_상품을_장바구니에_담을수있다() {

        //given
        Item item = Item.builder()
                .id(1L)
                .name("testItem")
                .build();

        CartAddRequest cartAddRequest = CartAddRequest.builder()
                .itemId(item.getId())
                .cartId(1L)
                .quantity(200)
                .build();

        ItemJpaRepository itemJpaRepository = mock(ItemJpaRepository.class);
        CartItemRepository cartItemRepository = mock(CartItemRepository.class);
        cartService = new CartService(itemJpaRepository, cartItemRepository);

        when(itemJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(cartItemRepository.existByItem(anyLong())).thenReturn(false);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(CartItem.builder().id(1L).build());

        //when
        Long saveId = cartService.cartAddItems(cartAddRequest);

        //then
        assertThat(saveId).isEqualTo(1L);
        verify(cartItemRepository).existByItem(anyLong());


    }
}


