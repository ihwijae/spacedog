package com.spacedog.item.service;


import com.spacedog.category.domain.Category;
import com.spacedog.category.domain.CategoryItem;
import com.spacedog.category.repository.CategoryItemRepository;
import com.spacedog.category.repository.CategoryRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.domain.ItemOptionGroupSpecification;
import com.spacedog.item.domain.ItemOptionSpecification;
import com.spacedog.item.dto.*;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.exception.NotEnoughStockException.CategoryDuplicate;
import com.spacedog.item.repository.ItemQueryRepository;
import com.spacedog.item.repository.ItemRepository;
import com.spacedog.item.repository.OptionRepository;
import com.spacedog.item.repository.OptionSpecsRepository;
import com.spacedog.member.domain.Member;
import com.spacedog.member.exception.MemberException;
import com.spacedog.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.spacedog.item.domain.QItemOptionGroupSpecification.itemOptionGroupSpecification;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final CategoryItemRepository categoryItemRepository;
    private final OptionRepository optionRepository;
    private final OptionSpecsRepository optionSpecsRepository;



    @Transactional
    public Long createItem(CreateItemRequest createItemRequest) {
        Member member = memberService.getMember();

        if(itemRepository.findByName(createItemRequest.getName()).isPresent()) {
            throw new NotEnoughStockException.ItemDuplicate("이미 존재하는 상품 이름 입니다");
        }

        if(member == null) {
            throw new MemberException("유저 정보를 불러올 수 없습니다");
        }

        Category category = categoryRepository.findById(createItemRequest.getCategoryId())
                .orElseThrow(() -> new CategoryDuplicate("카테고리를 찾을 수 없습니다"));


        Item item = ItemMapper.INSTANCE.toEntity(createItemRequest);
        item.addMember(member);
        itemRepository.save(item);

        CategoryItem categoryItem = CategoryItem.builder()
                .category(category)
                .item(item)
                .build();
        categoryItemRepository.save(categoryItem);


        createItemRequest.getOptionGroups().stream()
                .map(optionGroupRequest ->  {
                    ItemOptionGroupSpecification itemOptionGroup = ItemOptionGroupSpecification.builder()
                            .name(optionGroupRequest.getName())
                            .exclusive(optionGroupRequest.isExclusive())
                            .basic(optionGroupRequest.isBasic())
                            .item(item)
                            .build();


                    //옵션 그룹 저장
                    ItemOptionGroupSpecification saveItemOption = optionRepository.save(itemOptionGroup);

                    //옵션 사양 추가
                    optionGroupRequest.getOptionSpecsRequest()
                            .forEach(optionSpecsRequest -> {
                                ItemOptionSpecification itemOptionSpecs = ItemOptionSpecification.builder()
                                        .name(optionSpecsRequest.getName())
                                                .price(optionSpecsRequest.getPrice())
                                                        .optionGroupSpecification(saveItemOption)
                                                                .build();
                                optionSpecsRepository.save(itemOptionSpecs);
                            });

                    return saveItemOption;
                })
                .collect(Collectors.toList());

        return item.getId();
    }

    /**
     *
     * 양방향 매핑시에 사용 코드

//    @Transactional
//    public Long saveItem (CreateItemRequest request) {
//
//        Member member = memberService.getMember();
//
//        // Optional객체가 값을 가지고 있는지 확인 (isPresent)
//        if(itemRepository.findByName(request.getName()).isPresent()) {
//            throw new NotEnoughStockException.ItemDuplicate("이미 존재하는 상품 이름 입니다");
//        }
//
//        if(member == null) {
//            throw new MemberException("유저 정보를 불러올 수 없습니다");
//        }
//
//
//        Item item = ItemMapper.INSTANCE.toEntity(request);
//        item.addMember(member);
//
//        itemRepository.save(item);
//
//        Category category = categoryRepository.findById(request.getCategoryId())
//                .orElseThrow(() -> new NotEnoughStockException.CategoryDuplicate("해당 카테고리가 없습니다"));
//        itemRepository.save(item);
//
//        //카테고리 아이템 생성 및 저장
//        CategoryItem categoryItem = new CategoryItem();
//        categoryItem.addRelation(item, category);
//        categoryItemRepository.save(categoryItem);
//
//        // 옵션 그룹 불러오기
//        List<ItemOptionGroupSpecification> optionGroup = request.getOptionGroups()
//                .stream()
//                .map(optionGroupRequest -> ItemMapper.INSTANCE.toItemOptionGroup(optionGroupRequest))
//                .collect(Collectors.toList());
//        optionGroup.forEach(optionGroupSpecification -> optionGroupSpecification.addItem(item));
//        optionRepository.saveAll(optionGroup);
//
//        // 2. 각 옵션 그룹에 속한 옵션 스펙 추출 및 그룹과 연결
//        List<ItemOptionSpecification> optionSpecsList = new ArrayList<>();
//        request.getOptionGroups().forEach(optionGroupRequest -> {
//            // 해당 옵션 그룹에 속한 옵션 스펙 리스트 추출
//            List<ItemOptionSpecification> specsList = optionGroupRequest.getOptionSpecsRequest().stream()
//                    .map(ItemMapper.INSTANCE::toItemOptionSpecs) // Method reference 사용
//                    .collect(Collectors.toList());
//
//            // 현재 옵션 그룹 찾기
//            ItemOptionGroupSpecification optionGroupList = optionGroup.get(request.getOptionGroups().indexOf(optionGroupRequest));
//
//            // 각 옵션 스펙에 현재 옵션 그룹 설정 (양방향 매핑)
//            specsList.forEach(s -> s.addOptionGroup(optionGroupList));  // addOptionSpecs 메서드를 사용하여 관계를 설정
//
//            // 전체 옵션 스펙 리스트에 추가
//            optionSpecsList.addAll(specsList);
//        });
//
//
//// 3. 옵션 스펙 저장
//        optionSpecsRepository.saveAll(optionSpecsList);
//
//
//        return item.getId();
//
//
//    }
     */

    // 상품 전체 조회
    @Transactional(readOnly = true)
    public List<FindItemAllResponse> fineItemAll () {
        List<Item> all = itemRepository.findAll();
        return ItemMapper.INSTANCE.toFindItemAllResponse(all);
    }


    // 상품 검색
    @Transactional(readOnly = true)
    public List<SearchItemResponse> searchItem(SearchItemRequest request) {
        return itemQueryRepository.getItems(request);
    }


    // 상품 수정
    @Transactional
    public void itemEdit (Long id, ItemEditRequest request) {
        Member member = memberService.getMember();

        if(member == null) {
            throw new MemberException("유저 정보를 불러올 수 없습니다");
        }

        if(itemRepository.findByName(request.getName()).isPresent()) {
            throw new NotEnoughStockException.ItemDuplicate("중복된 상품 이름 입니다");
        }

        Item findItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotEnoughStockException.ItemNotFound("해당 상품이 존재하지 않습니다"));

        if(!findItem.getMemberId().equals(member.getId())) {
            throw new MemberException("상품을 등록한 사용자가 아닙니다");
        }

        findItem.itemUpdate(request);

//        // 기존 옵션 그룹과 사양을 가져와서 수정
//        List<ItemOptionGroupSpecification> findOptionGroup = optionRepository.findByItemId(findItem.getId());
//
//
//        request.getItemOption()
//                .forEach(optionGroupRequest -> {
//                    //존재하는 옵션그룹이면 업데이트, 아니면 추가
//                    ItemOptionGroupSpecification itemOptionGroupSpecification = findOptionGroup.stream()
//                            .filter(group -> group.getId().equals(optionGroupRequest.getId()))
//                            .findFirst()
//                            .orElseGet(() -> ItemOptionGroupSpecification.builder()
//                                    .name(optionGroupRequest.getName())
//                                    .exclusive(optionGroupRequest.isExclusive())
//                                    .basic(optionGroupRequest.isBasic())
//                                    .item(findItem)
//                                    .build());

            request.getItemOption()
                    .forEach(editOptionGroupRequest ->  {
                        //기존에 있는 옵션 그룹의면 수정, 아니면 새로 생성.
                        ItemOptionGroupSpecification itemOptionGroupSpecification = optionRepository.findByItemId(findItem.getId())
                                .orElseGet(() -> ItemOptionGroupSpecification.builder()
                                        .name(editOptionGroupRequest.getName())
                                        .exclusive(editOptionGroupRequest.isExclusive())
                                        .basic(editOptionGroupRequest.isBasic())
                                        .item(findItem)
                                        .build());
                        itemOptionGroupSpecification.update(editOptionGroupRequest);
                        ItemOptionGroupSpecification saved = optionRepository.save(itemOptionGroupSpecification);


                    //옵션 스펙 업데이트
                        editOptionGroupRequest.getOptionSpecsRequest()
                            .forEach(optionSpecsRequest -> {
                                ItemOptionSpecification optionSpecs = optionSpecsRepository.findByOptionGroupSpecification_Id(saved.getId())
                                        .orElseGet(() -> ItemOptionSpecification.builder()
                                                .name(optionSpecsRequest.getName())
                                                .price(optionSpecsRequest.getPrice())
                                                .optionGroupSpecification(saved)
                                                .build());
                                optionSpecs.update(optionSpecsRequest, saved);
                                optionSpecsRepository.save(optionSpecs);

                            });
                });

    }

    @Transactional
    public void itemDelete (Long id) {}



}
