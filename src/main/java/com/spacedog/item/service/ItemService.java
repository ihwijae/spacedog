package com.spacedog.item.service;


import com.spacedog.category.domain.Category;
import com.spacedog.category.repository.CategoryRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.FindItemAllResponse;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.repository.ItemRepository;
import com.spacedog.member.domain.Member;
import com.spacedog.member.exception.MemberException;
import com.spacedog.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;


    @Transactional
    public Long saveItem (CreateItemRequest request) {

        Member member = memberService.getMember();

        // Optional객체가 값을 가지고 있는지 확인 (isPresent)
        if(itemRepository.findByName(request.getName()).isPresent()) {
            throw new NotEnoughStockException.ItemDuplicate("이미 존재하는 상품 이름 입니다");
        }

        Category category = categoryRepository.findById((long) request.getCategoryId())
                .orElseThrow(() -> new NotEnoughStockException.CategoryDuplicate("해당 카테고리가 없습니다"));

        if(member == null) {
            throw new MemberException("유저 정보를 불러올 수 없습니다");
        }

        Item item = ItemMapper.INSTANCE.toEntity(request);
        item.addMember(member);

        Item resultItem = itemRepository.save(item);
        return resultItem.getId();


    }

    @Transactional(readOnly = true)
    public List<FindItemAllResponse> fineItemAll () {
        List<Item> all = itemRepository.findAll();
        return ItemMapper.INSTANCE.toFindItemAllResponse(all);
    }



}
