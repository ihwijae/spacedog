package com.spacedog.item.controller;

import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.FindItemAllResponse;
import com.spacedog.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/items")
    public List<FindItemAllResponse> findItems() {
        return itemService.fineItemAll();
    }
}
