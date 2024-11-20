package com.spacedog.cart.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.domain.QCartItem;
import com.spacedog.cart.dto.CartOptionResponse;
import com.spacedog.cart.dto.ItemCartResponse;
import com.spacedog.item.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spacedog.cart.domain.QCart.cart;
import static com.spacedog.cart.domain.QCartItem.cartItem;
import static com.spacedog.cart.domain.QCartOptionSpecs.cartOptionSpecs;
import static com.spacedog.item.domain.QItem.item;
import static com.spacedog.option.domain.QOptionGroupSpecification.optionGroupSpecification;
import static com.spacedog.option.domain.QOptionSpecification.optionSpecification;

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository {

    private final JPAQueryFactory queryFactory;
    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemJpaRepository.save(cartItem);
    }

    @Override
    public Optional<CartItem> findCartItems(Long itemId, List<Long> optionSpecsIds) {
        CartItem cartItem = queryFactory
                .selectFrom(QCartItem.cartItem)
                .where(QCartItem.cartItem.itemId.eq(itemId)
                        .and(QCartItem.cartItem.optionSpecsIds.any().in(optionSpecsIds)))
                .fetchOne();

        return Optional.ofNullable(cartItem);
    }

    @Override
    public Optional<CartItem> findCartItemsWithNotOptions(Long itemId) {
        CartItem cartItem = queryFactory
                .selectFrom(QCartItem.cartItem)
                .where(QCartItem.cartItem.itemId.eq(itemId))
                .fetchOne();

        return Optional.ofNullable(cartItem);

    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemJpaRepository.findById(id);
    }

    @Override
    public Boolean existByItemWithOptions(Long itemId, List<Long> optionSpecsIds) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(cartItem)
                .where(cartItem.itemId.eq(itemId)
                        .and(cartItem.optionSpecsIds.any().in(optionSpecsIds)))
                .fetchFirst();

        return fetchOne != null;

    }

    @Override
    public Boolean existByItem(Long itemId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(cartItem)
                .where(cartItem.itemId.eq(itemId))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public Map<Long, List<Item>> findItemMap(List<Long> itemIds) {
        List<Item> items = queryFactory
                .selectFrom(item)
                .where(item.id.in(itemIds))
                .fetch();

        Map<Long, List<Item>> itemMap = items.stream()
                .collect(Collectors.groupingBy(Item::getId));

        return itemMap;
    }

    @Override
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

    @Override
    public Map<Long, List<CartOptionResponse>> findCartSelectOptionMap(List<Long> cartItemIds) {
        List<CartOptionResponse> cartOptions = queryFactory
                .select(Projections.fields(CartOptionResponse.class,
                        optionSpecification.id,
                        optionSpecification.name,
                        optionSpecification.additionalPrice,
                        cartOptionSpecs.cartItemId
                ))
                .from(optionSpecification)
                .join(cartOptionSpecs).on(optionSpecification.id.eq(cartOptionSpecs.optionSpecsId))
                .where(cartOptionSpecs.cartItemId.in(cartItemIds))
                .fetch();

        Map<Long, List<CartOptionResponse>> cartOptionMap = cartOptions.stream()
                .collect(Collectors.groupingBy(CartOptionResponse::getCartItemId));

        return cartOptionMap;
    }

    @Override
    public Map<Long, List<String>> findCartOptionName(List<Long> cartItemIds) {
        List<Tuple> cartOptionNames = queryFactory
                .select(optionSpecification.name, cartOptionSpecs.cartItemId)
                .from(optionSpecification)
                .join(cartOptionSpecs).on(optionSpecification.id.eq(cartOptionSpecs.optionSpecsId))
                .where(cartOptionSpecs.cartItemId.in(cartItemIds))
                .fetch();

        Map<Long, List<String>> result = cartOptionNames.stream()
                .collect(Collectors.groupingBy(tuple -> tuple.get(cartOptionSpecs.cartItemId),
                        Collectors.mapping(
                                tuple -> tuple.get(optionSpecification.name),
                                Collectors.toList()
                        )
                ));
        return result;
    }

    /** 각 상품에 대한 전체 옵션 조회**/
    @Override
    public List<CartOptionResponse> findOptionAll(List<Long> itemIds) {
        return queryFactory
                .select(Projections.fields(CartOptionResponse.class,
                        optionSpecification.id,
                        optionSpecification.name,
                        optionSpecification.additionalPrice
                ))
                .from(optionSpecification)
                .join(optionGroupSpecification).on(optionSpecification.optionGroupSpecification.id.eq(optionGroupSpecification.id))
                .join(item).on(optionGroupSpecification.item.id.eq(item.id))
                .where(item.id.in(itemIds))
                .fetch();
    }

    @Override
    public List<Long> toCartItemIds(List<ItemCartResponse> result) {
        List<Long> cartItemIds = result.stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());

        return cartItemIds;
    }

    @Override
    public void delete(CartItem cartItem) {
        cartItemJpaRepository.delete(cartItem);
    }

    @Override
    public Optional<CartItem> findByItemIdWithMember(Long itemId, Long memberId) {
        CartItem cartItem = queryFactory
                .selectFrom(QCartItem.cartItem)
                .where(QCartItem.cartItem.itemId.eq(itemId).and(QCartItem.cartItem.cartId.eq(memberId)))
                .fetchOne();

        return Optional.ofNullable(cartItem);
    }

    @Override
    public void deleteAll(List<CartItem> cartItems) {
        cartItemJpaRepository.deleteAll(cartItems);
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
