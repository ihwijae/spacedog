package com.spacedog.order.service;

import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.order.impl.OrderCreateRequest;
import com.spacedog.order.impl.OrderCreateRequest.OrderItemCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartItemReader {

    private final CartItemRepository cartItemRepository;


    public List<CartItem> getCartItem(List<OrderItemCreate> orderItemCreate, Long memberId) {

        List<CartItem> cartItems = orderItemCreate.stream()
                .map(orderItem -> (
                                cartItemRepository.findByItemIdWithMember(orderItem.getItemId(), memberId)
                                        .orElseThrow(() -> new NotEnoughStockException("장바구니에 상품이 존재하지 않습니다"))
                        )
                )
                .collect(Collectors.toList());

        return cartItems;
    }


}
