package com.spacedog.item.impl;

import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.repository.ItemRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemReader {

    private final ItemRepositoryPort itemRepository;


    public Item findById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotEnoughStockException.ItemNotFound("item not found"));

        return item;
    }
}
