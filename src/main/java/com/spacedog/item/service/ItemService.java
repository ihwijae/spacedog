package com.spacedog.item.service;


import com.spacedog.category.domain.Category;
import com.spacedog.category.repository.CategoryRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.domain.ItemOptionGroupSpecification;
import com.spacedog.item.domain.ItemOptionSpecification;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.OptionGroupRequest;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.repository.ItemRepository;
import com.spacedog.item.repository.OptionRepository;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;


    @Transactional
    public Long saveItem (CreateItemRequest request) {

        Member member = memberService.getMember();

        // Optional객체가 값을 가지고 있는지 확인 (isPresent)
        if(itemRepository.findByName(request.getName()).isPresent()) {
            throw new NotEnoughStockException.ItemDuplicate("이미 존재하는 상품 이름 입니다");
        }

        Category category = categoryRepository.findById((long) request.getCategoryId())
                .orElseThrow(() -> new NotEnoughStockException.CategoryDuplicate("해당 카테고리가 없습니다"));


        Item item = Item.createItem(request, member, category);
        Item resultItem = itemRepository.save(item);



        // OptionGroupRequest를 ItemOptionGroupSpecification으로 매핑
        List<ItemOptionGroupSpecification> optionGroupSpecifications = request.getOptionGroups()
                .stream()
                .map(optionGroupRequest -> {
                    // ItemOptionGroupSpecification 생성
                    ItemOptionGroupSpecification optionGroupSpec = ItemMapper.INSTANCE.toItemOptionGroupSpecification(optionGroupRequest);
                    log.info("optionGroupSpec: {}", optionGroupSpec);

                    // 옵션 스펙 리스트 생성
                    List<ItemOptionSpecification> optionSpecs = optionGroupRequest.getOptionSpecsRequest() // <-- 여기에서 optionGroupRequest를 사용
                            .stream()
                                    .map(optionSpecsRequest -> ItemMapper.INSTANCE.toItemOptionItemOptionSpecification(optionSpecsRequest))
                                            .collect(Collectors.toList());

                    optionSpecs
                            .forEach(option -> optionGroupSpec.addOptionSpecs(option));

                    return optionGroupSpec; // ItemOptionGroupSpecification 반환
                })
                .collect(Collectors.toList());

        // 옵션 그룹 스펙 저장
        optionRepository.saveAll(optionGroupSpecifications);

        return resultItem.getId();
    }

}
