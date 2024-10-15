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
import java.util.Arrays;
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

    @Test
    @DisplayName("상품 삭제 테스트")
    void itemDelete () {


        itemService.itemDelete(1L);

    }




    private CreateItemRequest createItemRequest () {
        return CreateItemRequest.builder()
                .name("몬지 연어사료")
                .description("test description")
                .price(Money.wons(10000))
                .stockQuantity(999)
                .categoryIds(Arrays.asList(1L, 5L))
                .optionGroups(createOptionGroup())
                .build();
    }

    private List<OptionGroupRequest> createOptionGroup () {
        OptionGroupRequest testOptionGroup1 = OptionGroupRequest.builder()
                .name("용량")
                .exclusive(false)
                .basic(true)
                .optionSpecsRequest(createOptionSpecsVolume())
                .build();

        OptionGroupRequest testOptionGroup2 = OptionGroupRequest.builder()
                .name("나이별 사료")
                .exclusive(false)
                .basic(true)
                .optionSpecsRequest(createOptionSpecsAge())
                .build();

        return List.of(testOptionGroup1, testOptionGroup2);
        // java 9 부터 제공되는 불변 리스트를 생성하는 메서드
        // ArratList 와 다르게 리스트에 요소를 추가하거나 삭제할 수 없다.
    }

    private List<OptionSpecsRequest> createOptionSpecsVolume () {
        OptionSpecsRequest testOption1 = OptionSpecsRequest.builder()
                .name("2.5kg")
                .price(Money.wons(20000))
                .build();

        OptionSpecsRequest testOption2 = OptionSpecsRequest.builder()
                .name("5kg")
                .price(Money.wons(50000))
                .build();

        OptionSpecsRequest testOption3 = OptionSpecsRequest.builder()
                .name("7,5")
                .price(Money.wons(100000))
                .build();

        return List.of(testOption1, testOption2, testOption3);
    }

    private List<OptionSpecsRequest> createOptionSpecsAge () {
        OptionSpecsRequest testOption1 = OptionSpecsRequest.builder()
                .name("퍼피")
                .price(Money.wons(20000))
                .build();

        OptionSpecsRequest testOption2 = OptionSpecsRequest.builder()
                .name("어덜트")
                .price(Money.wons(20000))
                .build();

        OptionSpecsRequest testOption3 = OptionSpecsRequest.builder()
                .name("시니어")
                .price(Money.wons(20000))
                .build();

        return List.of(testOption1, testOption2, testOption3);
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