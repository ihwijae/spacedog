package com.spacedog.item.repository;

import com.spacedog.category.service.CategoryResponse;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;


import java.util.List;
import java.util.Map;
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
    public List<Item> findByIdIn(List<Long> ids);
    public Map<Long, List<OptionGroupResponse>> findOptionGroups(List<Long> itemIds);
    public Map<Long, List<OptionSpecsResponse>> findOptionSpecs(List<Long> optionGroupIds);
    public Map<Long, List<CategoryResponse>> findCategories(List<Long> itemIds);
    public List<ItemDetailResponse> findItemDetail(Long itemId);
}
