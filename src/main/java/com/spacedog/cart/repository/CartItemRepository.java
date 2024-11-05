package com.spacedog.cart.repository;


import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.dto.CartOptionResponse;
import com.spacedog.cart.dto.ItemCartResponse;
import com.spacedog.item.domain.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CartItemRepository {

    public CartItem save(CartItem cartItem);
    public Optional<CartItem> findCartItems(Long itemId, List<Long> optionSpecsIds);
    public Optional<CartItem> findCartItemsWithNotOptions(Long itemId);
    public Boolean existByItemWithOptions(Long itemId, List<Long> optionSpecsIds);
    public Boolean existByItem(Long itemId);
    public Map<Long, List<Item>> findItemMap(List<Long> itemIds);
    public List<ItemCartResponse> findItemCartDetail(Long cartId);
    public Map<Long, List<CartOptionResponse>> findCartSelectOptionMap(List<Long> cartItemIds);
    public Map<Long, List<String>> findCartOptionName(List<Long> cartItemIds);
    public List<CartOptionResponse> findOptionAll(List<Long> itemIds);
    public List<Long> toCartItemIds(List<ItemCartResponse> result);
    public Optional<CartItem> findById(Long id);
    void delete(CartItem cartItem);
    Optional<CartItem> findByItemIdWithMember(Long itemId, Long memberId);





}
