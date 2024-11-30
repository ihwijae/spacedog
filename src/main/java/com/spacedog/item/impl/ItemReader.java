package com.spacedog.item.impl;

import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.exception.NotEnoughStockException.ItemNotFound;
import com.spacedog.item.repository.ItemRepositoryPort;
import com.spacedog.order.domain.OrderItems;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemReader {

    private final ItemRepositoryPort itemRepository;


    public Item findById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFound("item not found"));

        return item;
    }





}
