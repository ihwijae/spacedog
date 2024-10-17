package com.spacedog.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.domain.QCartItem;
import com.spacedog.cart.domain.QCartOptionSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.spacedog.cart.domain.QCartItem.cartItem;
import static com.spacedog.cart.domain.QCartOptionSpecs.cartOptionSpecs;

@Repository
@RequiredArgsConstructor
public class CartItemQueryRepository {

    private final JPAQueryFactory queryFactory;


    public Optional<CartItem> findCartItems(Long itemId, List<Long> optionSpecsIds) {

        CartItem cartItem = queryFactory
                .selectFrom(QCartItem.cartItem)
                .where(QCartItem.cartItem.id.eq(itemId)
                        .and(QCartItem.cartItem.optionSpecsIds.any().in(optionSpecsIds)))
                .fetchOne();

        return Optional.ofNullable(cartItem);
    }

    @Transactional(readOnly = true)
    public Boolean exist(Long itemId, List<Long> optionSpecsIds) {
    Integer fetchOne = queryFactory
            .selectOne()
            .from(cartItem)
            .where(cartItem.id.eq(itemId)
                    .and(cartItem.optionSpecsIds.any().in(optionSpecsIds)))
            .fetchFirst();

    return fetchOne != null;
}

    


}


