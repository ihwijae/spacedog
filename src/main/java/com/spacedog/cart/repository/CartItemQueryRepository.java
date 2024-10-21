package com.spacedog.cart.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.cart.domain.*;
import com.spacedog.cart.dto.CartOptionGroupResponse;
import com.spacedog.cart.dto.CartOptionResponse;
import com.spacedog.cart.dto.CartResponse;
import com.spacedog.cart.dto.ItemCartResponse;
import com.spacedog.item.domain.Item;
import com.spacedog.item.domain.QItem;
import com.spacedog.item.domain.QItemOptionGroupSpecification;
import com.spacedog.item.domain.QItemOptionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spacedog.cart.domain.QCart.cart;
import static com.spacedog.cart.domain.QCartItem.cartItem;
import static com.spacedog.cart.domain.QCartOptionSpecs.cartOptionSpecs;
import static com.spacedog.item.domain.QItem.item;
import static com.spacedog.item.domain.QItemOptionGroupSpecification.itemOptionGroupSpecification;
import static com.spacedog.item.domain.QItemOptionSpecification.itemOptionSpecification;

@Repository
@RequiredArgsConstructor
public class CartItemQueryRepository {

    private final JPAQueryFactory queryFactory;


    public Optional<CartItem> findCartItems(Long itemId, List<Long> optionSpecsIds) {

        CartItem cartItem = queryFactory
                .selectFrom(QCartItem.cartItem)
                .where(QCartItem.cartItem.itemId.eq(itemId)
                        .and(QCartItem.cartItem.optionSpecsIds.any().in(optionSpecsIds)))
                .fetchOne();

        return Optional.ofNullable(cartItem);
    }

    public Optional<CartItem> findCartItemsWithNotOptions(Long itemId) {

        CartItem cartItem = queryFactory
                .selectFrom(QCartItem.cartItem)
                .where(QCartItem.cartItem.itemId.eq(itemId))
                .fetchOne();

        return Optional.ofNullable(cartItem);
    }

    @Transactional(readOnly = true)
    public Boolean existByItemWithOptions(Long itemId, List<Long> optionSpecsIds) {
    Integer fetchOne = queryFactory
            .selectOne()
            .from(cartItem)
            .where(cartItem.itemId.eq(itemId)
                    .and(cartItem.optionSpecsIds.any().in(optionSpecsIds)))
            .fetchFirst();

    return fetchOne != null;
}

@Transactional(readOnly = true)
public Boolean existByItem(Long itemId) {
    Integer fetchOne = queryFactory
            .selectOne()
            .from(cartItem)
            .where(cartItem.id.eq(itemId))
            .fetchFirst();

    return fetchOne != null;
}





public Map<Long, List<Item>> findItemMap(List<Long> itemIds) {
    List<Item> items = queryFactory
            .selectFrom(item)
            .where(item.id.in(itemIds))
            .fetch();

    Map<Long, List<Item>> itemMap = items.stream()
            .collect(Collectors.groupingBy(Item::getId));

    return itemMap;
}

public List<ItemCartResponse> findItemCartDetail(Long cartId) {
        return queryFactory
                .select(Projections.fields(ItemCartResponse.class,
                        cartItem.id,
                        cartItem.itemId,
                        cartItem.itemName,
                        Expressions.asNumber(cartId).as(cartItem.cartId),
                        cartItem.quantity
                        ))
                .from(cartItem)
                .join(cart).on(cartItem.cartId.eq(cart.id))
                .where(cartItem.cartId.eq(cartId))
                .fetch();
}



public Map<Long, List<CartOptionResponse>> findCartSelectOptionMap(List<Long> cartItemIds) {
    List<CartOptionResponse> cartOptions = queryFactory
            .select(Projections.fields(CartOptionResponse.class,
                    itemOptionSpecification.id,
                    itemOptionSpecification.name,
                    itemOptionSpecification.additionalPrice,
                    cartOptionSpecs.cartItemId
            ))
            .from(itemOptionSpecification)
            .join(cartOptionSpecs).on(itemOptionSpecification.id.eq(cartOptionSpecs.optionSpecsId))
            .where(cartOptionSpecs.cartItemId.in(cartItemIds))
            .fetch();

    Map<Long, List<CartOptionResponse>> cartOptionMap = cartOptions.stream()
            .collect(Collectors.groupingBy(CartOptionResponse::getCartItemId));

    return cartOptionMap;
}

public Map<Long, List<String>> findCartOptionName(List<Long> cartItemIds) {
    List<Tuple> cartOptionNames = queryFactory
            .select(itemOptionSpecification.name, cartOptionSpecs.cartItemId)
            .from(itemOptionSpecification)
            .join(cartOptionSpecs).on(itemOptionSpecification.id.eq(cartOptionSpecs.optionSpecsId))
            .where(cartOptionSpecs.cartItemId.in(cartItemIds))
            .fetch();

    Map<Long, List<String>> result = cartOptionNames.stream()
            .collect(Collectors.groupingBy(tuple -> tuple.get(cartOptionSpecs.cartItemId),
                    Collectors.mapping(
                            tuple -> tuple.get(itemOptionSpecification.name),
                            Collectors.toList()
                    )
            ));
    return result;
}

//public Map<Long, List<CartOptionGroupResponse>> findCartSelectOptionGroupMap(List<Long> cartItemIds) {
//        queryFactory
//                .select(Projections.fields(CartOptionGroupResponse.class,
//                        itemOptionGroupSpecification.name))
//                .from(itemOptionGroupSpecification)
//}

/** 각 상품에 대한 전체 옵션 조회**/
public List<CartOptionResponse> findOptionAll(List<Long> itemIds) {
    return queryFactory
            .select(Projections.fields(CartOptionResponse.class,
                    itemOptionSpecification.id,
                    itemOptionSpecification.name,
                    itemOptionSpecification.additionalPrice
                    ))
            .from(itemOptionSpecification)
            .join(itemOptionGroupSpecification).on(itemOptionSpecification.optionGroupSpecification.id.eq(itemOptionGroupSpecification.id))
            .join(item).on(itemOptionGroupSpecification.item.id.eq(item.id))
            .where(item.id.in(itemIds))
            .fetch();
}

public List<Long> toCartItemIds(List<ItemCartResponse> result) {
    List<Long> cartItemIds = result.stream()
            .map(o -> o.getId())
            .collect(Collectors.toList());

    return cartItemIds;
}



//    private Map<Long, List<OptionGroupResponse>> findOptionGroupMap(List<Long> itemIds) {
//        List<OptionGroupResponse> optionGroupResponses = query
//                .select(Projections.fields(OptionGroupResponse.class,
//                        itemOptionGroupSpecification.name,
//                        itemOptionGroupSpecification.exclusive,
//                        itemOptionGroupSpecification.basic))
//                .from(itemOptionGroupSpecification)
//                .join(item).on(itemOptionGroupSpecification.item.id.eq(item.id))
//                .where(itemOptionGroupSpecification.item.id.in(itemIds))
//                .fetch();
//
//        Map<Long, List<OptionGroupResponse>> optionGroupMap = optionGroupResponses.stream()
//                .collect(Collectors.groupingBy(optionGroup -> optionGroup.getId()));
//
//        return optionGroupMap;
//    }




}


