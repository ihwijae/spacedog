package com.spacedog.item.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.FindItemAllResponse;
import com.spacedog.item.dto.SearchItemRequest;
import com.spacedog.item.dto.SearchItemResponse;
import com.spacedog.item.repository.ItemRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemManager {

    private final ItemRepositoryPort repository;


    public Long save(Item item) {
        Item saveItem = repository.save(item);
        return saveItem.getId();
    }

    public void delete(Long id) {
        repository.delete(id);
    }




}
