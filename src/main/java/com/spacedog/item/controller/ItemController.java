package com.spacedog.item.controller;

import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {


    private final ItemService itemService;


    @PostMapping("/items")
    public String itemCreate(@RequestBody CreateItemRequest createItemRequest) {
        Long id = itemService.saveItem(createItemRequest);

        return id + "아이템 저장 완료";
    }
}
