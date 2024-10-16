package com.spacedog.cart.service;

import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.dto.CartAddRequest;
import com.spacedog.cart.exception.CartException;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.cart.repository.CartRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;


    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        TestingAuthenticationToken mockAuthentication = new TestingAuthenticationToken("lhj@naver.com", "12345678");
        context.setAuthentication(mockAuthentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    void addCart() {

        // given
        CartAddRequest cartAddRequest = CartAddRequest.builder()
                .cartId(1L)
                .itemId(1L)
                .quantity(5)
                .optionSpecsIds(Arrays.asList(1L, 4L))
                .build();

        // when
        Long cartId = cartService.cartAddItems(cartAddRequest);
        CartItem cartItem = cartItemRepository.findById(cartId)
                .orElseThrow(() -> new CartException("test 장바구니를 불러올 수 없습니다"));

        // then
        Assertions.assertThat(cartId).isEqualTo(cartItem.getId());

    }


}