package com.spacedog.item.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.FindItemAllResponse;
import com.spacedog.item.dto.ItemDetailResponse;
import com.spacedog.item.dto.SearchItemRequest;
import com.spacedog.item.dto.SearchItemResponse;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.exception.NotEnoughStockException.ItemNotFound;
import com.spacedog.item.repository.ItemRepositoryPort;
import com.spacedog.member.domain.Member;
import com.spacedog.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    public void validateRegister(Long registerId, Long memberId) {


        if(registerId.equals(memberId)) {
            throw new MemberException("상품을 등록한 회원이 아닙니다");
        }
    }

    public Item find(Long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFound("상품을 찾을수 없습니다"));

        return item;
    }


    public List<FindItemAllResponse> findItemsAll(int pageNo, int pageSize) {

        return repository.findItemsAll(pageNo, pageSize);
    }

    public List<SearchItemResponse> getItems(SearchItemRequest request) {

        return repository.getItems(request);

    }

    public Optional<Item> findByName(String name) {
        return repository.findByName(name);
    }

    public Item findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFound("상품을 찾을 수 없습니다"));
    }

    public List<ItemDetailResponse> itemDetail(Long itemId) {
//        return repository.itemDetail(itemId);

        List<ItemDetailResponse> itemDetail = repository.findItemDetail(itemId);


    }







}
