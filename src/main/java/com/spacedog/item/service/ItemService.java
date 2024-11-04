package com.spacedog.item.service;



import com.spacedog.category.service.CategoryService;
import com.spacedog.item.domain.Item;

import com.spacedog.item.dto.*;

import com.spacedog.item.repository.*;
import com.spacedog.member.domain.Member;
import com.spacedog.member.exception.MemberException;
import com.spacedog.option.service.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.spacedog.item.exception.NotEnoughStockException.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {


    private final CategoryService categoryService;
    private final OptionService optionService;
    private final ItemRepositoryPort itemRepositoryPort;



    @Transactional
    public Long createTemporaryItem() {
        Item item = itemRepositoryPort.save(new Item());
        return item.getId();
    }



    @Transactional
    public Long createItem(CreateItemRequest createItemRequest, Member member) {

        boolean isValid = itemRepositoryPort.existByName(createItemRequest.getName());
//        Item item = Item.createItem(itemId, createItemRequest, exist); -> 이건 새로생성하는거니까 수정해야함

        Item item = itemRepositoryPort.findById(createItemRequest.getId())
                .orElseThrow(() -> new ItemNotFound("상품을 찾을수 없습니다"));
        item.finalCreateItem(createItemRequest, isValid);


        item.addMember(member);
        Item saveItem = itemRepositoryPort.save(item);

        categoryService.saveCategoryItem(createItemRequest, item);


        /** 상품의 옵션이 있으면 옵션을 저장 **/
        if(createItemRequest.getOptionGroups() != null) {
            optionService.saveOptionWithItem(createItemRequest, item);
        }

        return createItemRequest.getId();
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
    @Cacheable(cacheNames = "items", key = "#offset + '-' + #limit") //페이징 처리된 데이터를 캐시
    //cacheNames -> items라는 이름의 캐시 저장소에 캐시를 저장한다. (캐시의 그룹이나 유형을 정의하는것)
    //key -> #pageable을 키로 사용하여 요청에 따라 다른 페이징 결과를 캐시에 저장하겠다. 각 페이징 요청은 서로다른 key를 생성하므로 결과가 다르게 캐시된다. (각 캐시 항목을 구별하는 고유한 식별자)
    public List<FindItemAllResponse> fineItemAll (int pageNo, int pageSize ) {
        List<FindItemAllResponse> itemsAll = itemRepositoryPort.findItemsAll(pageNo, pageSize);

        return itemsAll;

    }


    // 상품 검색
    @Transactional(readOnly = true)
    public List<SearchItemResponse> searchItem(SearchItemRequest request) {
        return itemRepositoryPort.getItems(request);
    }


    // 상품 수정
    @Transactional
    public void itemEdit (Long id, ItemEditRequest request, Member member) {


        if (itemRepositoryPort.findByName(request.getName()).isPresent()) {
            throw new ItemDuplicate("중복된 상품 이름 입니다");
        }

        Item findItem = itemRepositoryPort.findById(id)
                .orElseThrow(() -> new ItemNotFound("해당 상품이 존재하지 않습니다"));


        if (!findItem.getMemberId().equals(member.getId())) {
            throw new MemberException("상품을 등록한 사용자가 아닙니다");
        }

        findItem.itemUpdate(request);

        optionService.editOptionWithItem(request, findItem);


        //단일 옵션 일때
//        request.getItemOption()
//                    .forEach(editOptionGroupRequest ->  {
//                        //기존에 있는 옵션 그룹의면 수정, 아니면 새로 생성.
//                        ItemOptionGroupSpecification itemOptionGroupSpecification = optionRepository.findByItemId(findItem.getId())
//                                .orElseGet(() -> ItemOptionGroupSpecification.builder()
//                                        .name(editOptionGroupRequest.getName())
//                                        .exclusive(editOptionGroupRequest.isExclusive())
//                                        .basic(editOptionGroupRequest.isBasic())
//                                        .item(findItem)
//                                        .build());
//                        itemOptionGroupSpecification.update(editOptionGroupRequest);
//                        ItemOptionGroupSpecification saved = optionRepository.save(itemOptionGroupSpecification);
//
//
//                    //옵션 스펙 업데이트
//                        editOptionGroupRequest.getOptionSpecsRequest()
//                            .forEach(optionSpecsRequest -> {
//                                ItemOptionSpecification optionSpecs = optionSpecsRepository.findByOptionGroupSpecification_Id(saved.getId())
//                                        .orElseGet(() -> ItemOptionSpecification.builder()
//                                                .name(optionSpecsRequest.getName())
//                                                .price(optionSpecsRequest.getPrice())
//                                                .optionGroupSpecification(saved)
//                                                .build());
//                                optionSpecs.update(optionSpecsRequest, saved);
//                                optionSpecsRepository.save(optionSpecs);
//
//                            });
//                });


    }


    @Transactional
    public void itemDelete (Long itemId, Member member) {



        Item item = itemRepositoryPort.findById(itemId)
                .orElseThrow(() -> new ItemNotFound("아이템을 찾을 수 없습니다"));

        //상품을 등록한 회원과 로그인한 회원이 다르면 예외
        if(!item.getMemberId().equals(member.getId())) {
            throw new MemberException("상품을 등록한 회원이 아닙니다");
        }

        // 카테고리 아이템 삭제
        categoryService.deleteCategoryItem(itemId);

        // 옵션 삭제
        optionService.deleteOptionWithItem(itemId);

        itemRepositoryPort.delete(itemId);
    }



    // 상품 상세 조회
    @Transactional(readOnly = true)
    public List<ItemDetailResponse> itemDetail(Long itemId) {
        return itemRepositoryPort.itemDetail(itemId);

    }

    private void validateMember(Member member) {

        if(member == null) {
            throw new MemberException("유저 정보를 불러올 수 없습니다");
        }
    }






}
