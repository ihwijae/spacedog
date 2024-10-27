package com.spacedog.item.controller;

import com.spacedog.global.ApiResponse;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;
import com.spacedog.item.service.ItemService;
import com.spacedog.member.domain.Member;
import com.spacedog.member.exception.MemberException;
import com.spacedog.member.service.MemberService;
import com.spacedog.member.service.MemberValidate;
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
    private final MemberService memberService;
    private final MemberValidate memberValidate;


    // 상품 등록
    @PostMapping("/items")
    public String itemCreate(@RequestBody CreateItemRequest createItemRequest) {

        Member member = memberService.getMember();
        validateMember(member);

        Long id = itemService.createItem(createItemRequest, member);
        return id + "아이템 저장 완료";
    }



    // 상품 전체 조회
    @GetMapping("/items")
    public ApiResponse<List<FindItemAllResponse>> findItems(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        List<FindItemAllResponse> findItemAllResponses = itemService.fineItemAll(pageNo, pageSize);
        return ApiResponse.success(findItemAllResponses, "조회 했습니다");
    }



    // 상품 검색
    @GetMapping("/items/search")
    public List<SearchItemResponse> searchItems(@ModelAttribute SearchItemRequest searchItemRequest) {
        return itemService.searchItem(searchItemRequest);

    }

    // 상품 수정
    @PatchMapping("/items")
    public ApiResponse<String> itemEdit(@RequestParam Long id, @RequestBody ItemEditRequest itemEditRequest) {
        Member member = memberService.getMember();
        validateMember(member);

        itemService.itemEdit(id, itemEditRequest, member);
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
        Member member = memberService.getMember();
        validateMember(member);


        itemService.itemDelete(itemId, member);
        return ApiResponse.success(null, "삭제 되었습니다.");
    }


    private void validateMember(Member member) {

        if(member == null) {
            throw new MemberException("유저 정보를 불러올 수 없습니다");
        }
    }

}
