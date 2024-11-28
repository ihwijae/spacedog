package com.spacedog.item.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.repository.ItemRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemManager {

    private final ItemRepositoryPort repository;


    public Long save(Item item) {
        Item saveItem = repository.save(item);
        return saveItem.getId();
    }

}
