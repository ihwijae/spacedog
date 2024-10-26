package com.spacedog.item.repository;

import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;


import java.util.List;
import java.util.Optional;

public interface ItemRepositoryPort {

    Optional<Item> findById(Long id);
    Item save(Item item);
    void delete(Long id);
    void deleteAll(Iterable<Item> items);
    void update(Long itemId, ItemEditRequest request);
    Optional<Item> findByName(String name);

    // queryDSL
    public boolean existByName(String name);
    public List<SearchItemResponse> getItems(SearchItemRequest request);
    public List<FindItemAllResponse> findItemsAll(int pageNo, int pageSize);
    public Optional<Item> findByItemWithCategory(Long id);
    public List<ItemDetailResponse> itemDetail(Long itemId);

}
