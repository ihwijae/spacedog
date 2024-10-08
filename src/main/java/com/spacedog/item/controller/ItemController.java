package com.spacedog.item.controller;

import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.FindItemAllResponse;
import com.spacedog.item.dto.SearchItemRequest;
import com.spacedog.item.dto.SearchItemResponse;
import com.spacedog.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {


    private final ItemService itemService;


    // 상품 등록
    @PostMapping("/items")
    public String itemCreate(@RequestBody CreateItemRequest createItemRequest) {
        Long id = itemService.createItem(createItemRequest);

        return id + "아이템 저장 완료";
    }

    // 상품 전체 조회
    @GetMapping("/items")
    public List<FindItemAllResponse> findItems() {
        return itemService.fineItemAll();
    }

    // 상품 검색
    @GetMapping("/items/search")
    public List<SearchItemResponse> searchItems(@ModelAttribute SearchItemRequest searchItemRequest) {
        return itemService.searchItem(searchItemRequest);
    }

    // 상품 수정



}