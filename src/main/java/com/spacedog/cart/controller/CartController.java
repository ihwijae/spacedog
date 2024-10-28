package com.spacedog.cart.controller;

import com.spacedog.cart.dto.CartAddRequest;
import com.spacedog.cart.dto.CartResponse;
import com.spacedog.cart.service.CartService;
import com.spacedog.global.ApiResponse;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;


    @GetMapping("/cart/{cartId}")
    public ApiResponse<CartResponse> getCart(@PathVariable Long cartId) {
        CartResponse cart = cartService.getCart(cartId);

        return ApiResponse.success(cart, "장바구니 조회");
    }

    @PostMapping("/cart/{cartId}")
    public ApiResponse<Long> addCart(@RequestBody CartAddRequest cartAddRequest) {

        Long resultId = cartService.cartAddItems(cartAddRequest);

        return ApiResponse.success(resultId, "장바구니에 담았습니다");
    }
}
