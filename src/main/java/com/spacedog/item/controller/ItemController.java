package com.spacedog.item.controller;

import com.spacedog.global.ApiResponse;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;
import com.spacedog.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ApiResponse<Page<FindItemAllResponse>> findItems(@PageableDefault (page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<FindItemAllResponse> findItemAllResponses = itemService.fineItemAll(pageable);
        return ApiResponse.success(findItemAllResponses, "아이템 전체조회 성공");
    }

    // 상품 검색
    @GetMapping("/items/search")
    public List<SearchItemResponse> searchItems(@ModelAttribute SearchItemRequest searchItemRequest) {
        return itemService.searchItem(searchItemRequest);

    }

    // 상품 수정
    @PatchMapping("/items")
    public ApiResponse<String> itemEdit(@RequestParam Long id, @RequestBody ItemEditRequest itemEditRequest) {
        itemService.itemEdit(id, itemEditRequest);
        return ApiResponse.success(null, "수정 완료");
    }

    // 상품 상세 조회
    @GetMapping("/items/{itemId}")
    public ApiResponse<List<ItemDetailResponse>> itemDetail(@PathVariable Long itemId) {
        List<ItemDetailResponse> itemDetailResponses = itemService.itemDetail(itemId);
        return ApiResponse.success(itemDetailResponses, "상세 조회 완료");
    }

    // 상품 삭제
    @DeleteMapping("/items/{itemId}")
    public ApiResponse<String> itemDelete(@PathVariable Long itemId) {
        itemService.itemDelete(itemId);
        return ApiResponse.success(null, "삭제 되었습니다.");
    }

}
