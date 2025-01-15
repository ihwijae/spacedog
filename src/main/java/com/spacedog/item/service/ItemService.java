package com.spacedog.item.service;



import com.spacedog.category.component.CategoryFinder;
import com.spacedog.category.component.CategoryItemFinder;
import com.spacedog.category.component.CategoryManager;
import com.spacedog.category.domain.Category;
import com.spacedog.category.domain.CategoryItem;
import com.spacedog.category.exception.CategoryNotFoundException;
import com.spacedog.category.service.CategoryService;
import com.spacedog.item.domain.Item;

import com.spacedog.item.dto.*;

import com.spacedog.item.repository.*;
import com.spacedog.member.domain.Member;
import com.spacedog.member.exception.MemberException;
import com.spacedog.member.service.MemberReader;
import com.spacedog.option.domain.OptionGroupSpecification;
import com.spacedog.option.domain.OptionSpecification;
import com.spacedog.option.service.OptionFinder;
import com.spacedog.option.service.OptionGroupManager;
import com.spacedog.option.service.OptionManager;
import com.spacedog.option.service.OptionService;
import com.spacedog.stock.service.StockDomainManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.spacedog.item.exception.NotEnoughStockException.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {


    private final OptionService optionService;
    private final OptionManager optionManager;
    private final StockDomainManager stockDomainManager;
    private final ItemFinder itemFinder;
    private final OptionGroupManager optionGroupManager;
    private final ItemManager itemManager;
    private final MemberReader memberReader;
    private final CategoryFinder categoryFinder;
    private final CategoryManager categoryManager;
    private final CategoryItemFinder categoryItemFinder;
    private final OptionFinder optionFinder;


    @Transactional
    public Long createTemporaryItem() {
        return itemManager.save(new Item());
    }



    @Transactional
    public Long createItem(CreateItemRequest createItemRequest, Member member) {


        itemFinder.validateItemName(createItemRequest.getName());
        Item item = itemFinder.find(createItemRequest.getId());

        item.finalCreateItem(createItemRequest);
        item.addMember(member);


        List<Long> categoryIds = createItemRequest.getCategoryIds();

        if(categoryIds.isEmpty()) {
            throw new CategoryNotFoundException.CategoryOfNot("카테고리가 없으면 등록할 수 없습니다");
        }

        categoryIds.forEach(c -> {
            Category findCategory = categoryFinder.findCategory(c);

            CategoryItem categoryItem = CategoryItem.createCategoryItem(findCategory, item);
            categoryManager.save(categoryItem);
        });



        List<OptionGroupRequest> optionGroups = createItemRequest.getOptionGroups();

        if(optionGroups == null || optionGroups.isEmpty()) {
            Long defaultOptionId = optionManager.saveDefaultOption(item);
            stockDomainManager.createNotOptionItemStock(item.getId(), defaultOptionId, createItemRequest.getStockQuantity());
        } else {
            optionGroups.forEach(optionGroup -> {
                OptionGroupSpecification optionGroupResult = optionGroupManager.createOptionGroup(item, optionGroup);
                List<OptionSpecsRequest> optionSpecsRequest = optionGroup.getOptionSpecsRequest();

                optionSpecsRequest.forEach(optionSpec -> {
                    Long optionId = optionManager.saveOptionV3(optionGroupResult, optionSpec);
                    stockDomainManager.createExistOptionItemStock(item.getId(), optionId, optionSpec.getStockQuantity());
                });
            });
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
    public List<FindItemAllResponse> fineItemAll (int pageNo, int pageSize ) {
        List<FindItemAllResponse> itemsAll = itemFinder.findItemsAll(pageNo, pageSize);

        return itemsAll;

    }


    // 상품 검색
    @Transactional(readOnly = true)
    public List<SearchItemResponse> searchItem(SearchItemRequest request) {
        return itemFinder.getItems(request);
    }


    // 상품 수정
    @Transactional
    public void itemEdit (Long id, ItemEditRequest request) {


        // 상품이름 검증
        itemFinder.validateItemName(request.getName());

        Item findItem = itemFinder.findById(id);
        Member member = memberReader.getMember();


        //상품을 등록한 회원인지 검증
        itemFinder.validateRegister(findItem.getMemberId(), member.getId());

        findItem.itemUpdate(request);

        optionService.editOptionWithItem(request, findItem);

    }


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




    @Transactional
    public void itemDelete (Long itemId, Member member) {

        Item item = itemFinder.findById(itemId);


        //상품을 등록한 회원과 로그인한 회원이 다르면 예외
        if(!item.getMemberId().equals(member.getId())) {
            throw new MemberException("상품을 등록한 회원이 아닙니다");
        }

        // 카테고리 아이템 삭제
        List<CategoryItem> categoryItems = categoryItemFinder.findCategoryItemsWithItem(itemId);
        categoryManager.deleteAll(categoryItems);

        // 옵션 삭제
        List<OptionGroupSpecification> optionGroups = optionFinder.findOptionGroups(itemId);
        List<Long> optionGroupIds = optionFinder.findOptionGroupIds(optionGroups);
        List<OptionSpecification> optionSpecifications = optionFinder.findOptionSpecifications(optionGroupIds);

        optionManager.deleteOption(optionGroups);
        optionManager.deleteOptionSpecs(optionSpecifications);

        itemManager.delete(itemId);
    }



    // 상품 상세 조회
    @Transactional(readOnly = true)
    public List<ItemDetailResponse> itemDetail(Long itemId) {
        return itemFinder.itemDetail(itemId);

    }







}
