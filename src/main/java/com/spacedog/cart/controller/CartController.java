package com.spacedog.cart.controller;

import com.spacedog.cart.dto.CartResponse;
import com.spacedog.cart.service.CartService;
import com.spacedog.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;


    @RequestMapping("/cart/{cartId}")
    public ApiResponse<CartResponse> getCart(@PathVariable Long cartId) {
        CartResponse cart = cartService.getCart(cartId);

        return ApiResponse.success(cart, "장바구니 조회");
    }
}
