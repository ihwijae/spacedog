package com.spacedog.item.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.repository.ItemRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemFinder {

    private final ItemRepositoryPort repository;


    public void validateItemName(String itemName) {
        boolean isValid = repository.existByName(itemName);

        if(isValid) {
            throw new NotEnoughStockException.ItemDuplicate("해당 상품이 이미 존재합니다.");
        }
    }

    public Item find(Long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotEnoughStockException.ItemNotFound("상품을 찾을수 없습니다"));

        return item;
    }
}
