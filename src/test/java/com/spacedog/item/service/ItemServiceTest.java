package com.spacedog.item.service;

import com.spacedog.generic.Money;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;
import com.spacedog.item.repository.ItemRepository;
import com.spacedog.member.repository.MemberRepository;
import com.spacedog.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ItemServiceTest {


    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;



    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        TestingAuthenticationToken mockAuthentication = new TestingAuthenticationToken("lhj@gmail.com", "12345678");
        context.setAuthentication(mockAuthentication);
        SecurityContextHolder.setContext(context);
    }


    @Test
    @DisplayName("상품 생성 테스트")//jwt 인증테스트
    void itemCreate() {

        //given
        CreateItemRequest itemRequest = createItemRequest();


        //when
        Long resultId = itemService.createItem(itemRequest);
        Item findItem = itemRepository.findById(resultId).orElseThrow();

        //then
        assertNotNull(resultId); //아이템 id가 null이 아닌지 확인
        Assertions.assertThat(resultId).isEqualTo(findItem.getId());
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void itemEdit () {

        //given
        ItemEditRequest itemEditRequest = editItemRequest();


        //when
        itemService.itemEdit(1L, itemEditRequest);
        Item item = itemRepository.findById(1L).orElseThrow();

        //then
        assertNotNull(item.getId());
    }


    private CreateItemRequest createItemRequest () {
        return CreateItemRequest.builder()
                .name("아투 퍼피사료")
                .description("test description")
                .price(Money.wons(10000))
                .stockQuantity(999)
                .categoryId(5L)
                .optionGroups(createOptionGroup())
                .build();
    }

    private List<OptionGroupRequest> createOptionGroup () {
        OptionGroupRequest testOptionGroup = OptionGroupRequest.builder()
                .name("용량")
                .exclusive(false)
                .basic(true)
                .optionSpecsRequest(createOptionSpecs())
                .build();
        return List.of(testOptionGroup);
        // java 9 부터 제공되는 불변 리스트를 생성하는 메서드
        // ArratList 와 다르게 리스트에 요소를 추가하거나 삭제할 수 없다.
    }

    private List<OptionSpecsRequest> createOptionSpecs () {
        OptionSpecsRequest testOption = OptionSpecsRequest.builder()
                .name("2.5kg")
                .price(Money.wons(20000))
                .build();
        return List.of(testOption);
    }

    private ItemEditRequest editItemRequest () {
        return ItemEditRequest.builder()
                .name("test item edit")
                .description("test edit description")
                .price(Money.wons(8900))
                .stockQuantity(2500)
                .itemOption(editOptionGrups())
                .build();
    }

    private List<EditOptionGroupRequest> editOptionGrups() {
        EditOptionGroupRequest testEditOptionGroup = EditOptionGroupRequest.builder()
                .name("Test edit option group")
                .exclusive(true)
                .basic(true)
                .optionSpecsRequest(editOptionSpecs())
                .id(1L)
                .build();
        return List.of(testEditOptionGroup);
    }

    private List<EditOptionSpecsRequest> editOptionSpecs() {
        EditOptionSpecsRequest testEditOption = EditOptionSpecsRequest.builder()
                .name("Test edit option")
                .price(Money.wons(9800))
                .id(1L)
                .build();
        return List.of(testEditOption);
    }

}