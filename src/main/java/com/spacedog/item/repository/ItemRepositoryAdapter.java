package com.spacedog.item.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.spacedog.item.domain.QItem.item;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepositoryPort {

    private final ItemJpaRepository repository;
    private final ItemQueryRepository queryRepository;

    @Override
    public List<Item> findByIdIn(List<Long> ids) {
        List<Item> Items = repository.findByIdIn(ids);
        return Items;
    }

    @Override
    public Optional<Item> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    @Override
    public void delete(Long id) {
        Item item = repository.findById(id).orElseThrow();
        repository.delete(item);
    }

    @Override
    public void deleteAll(Iterable<Item> items) {
        repository.deleteAll(items);
    }

    @Override
    public void update(Long itemId, ItemEditRequest request) {
        Item findItem = repository.findById(itemId).orElseThrow();
        findItem.itemUpdate(request);
    }


    @Override
    public boolean existByName(String name) {

        return queryRepository.existByName(name);
    }

    @Override
    public List<SearchItemResponse> getItems(SearchItemRequest request) {

        return queryRepository.getItems(request);

    }

    @Override
    public List<FindItemAllResponse> findItemsAll(int pageNo, int pageSize) {

        return queryRepository.findItemsAll(pageNo, pageSize);

    }

    @Override
    public Optional<Item> findByItemWithCategory(Long id) {
        return queryRepository.findByItemWithCategory(id);
    }


    @Override
    public List<ItemDetailResponse> itemDetail(Long itemId) {
       return queryRepository.itemDetail(itemId);
    }





    private BooleanExpression LikeItemName(String searchName) {
        if(StringUtils.hasText(searchName)) {
            return item.name.like("%" + searchName + "%");
        }
        return null;
    }

    private BooleanExpression LikeItemContent(String searchContent) {
        if(StringUtils.hasText(searchContent)) {
            return item.description.like("%" + searchContent + "%");
        }
        return null;

    }






}
