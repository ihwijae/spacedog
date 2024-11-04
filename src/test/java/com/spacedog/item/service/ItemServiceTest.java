package com.spacedog.item.service;

import com.spacedog.category.service.CategoryService;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;
import com.spacedog.item.repository.ItemJpaRepository;
import com.spacedog.item.repository.ItemRepositoryPort;
import com.spacedog.member.domain.Member;
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


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ItemServiceTest  {




    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemJpaRepository itemJpaRepository;
    @Autowired
    private ItemRepositoryPort itemRepositoryPort;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CategoryService categoryService;




    @BeforeEach
    void setUp() {

//        memberService.singUp(
//                MemberSignUpRequest.builder()
//                        .name("testUser")
//                        .email("testUser@email.com")
//                        .password("testPassword")
//                        .checkedPassword("testPassword")
//                        .nickname("testNickName")
//                        .build()
//        );

//        Category.builder()
//                .name("testCategory1")
//                .depth(0L)
//                .children(Arrays.asList(Category))

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        TestingAuthenticationToken mockAuthentication = new TestingAuthenticationToken("test@email.com", "12345678");
        context.setAuthentication(mockAuthentication);
        SecurityContextHolder.setContext(context);
    }


    @Test
    @DisplayName("상품 생성 테스트")//jwt 인증테스트
    void itemCreate() {

        //given
        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .id(1L)
                .name("testItem")
                .description("testItemDescription")
                .price(5000)
                .build();
        Member member = Member.builder()
                .email("test@email.com")
                .build();


        //when
        Long resultId = itemService.createItem(createItemRequest, member);
        Item findItem = itemRepositoryPort.findById(resultId).orElseThrow();

        //then
        assertNotNull(resultId); //아이템 id가 null이 아닌지 확인
        Assertions.assertThat(resultId).isEqualTo(findItem.getId());
    }

    @Test
    @DisplayName("상품 수정 테스트")

    void itemEdit () {

        //given
        ItemEditRequest itemEditRequest = editItemRequest();
        Member member = Member.builder()
                .email("test@email.com")
                .build();


        //when
        itemService.itemEdit(1L, itemEditRequest, member);
        Item item = itemJpaRepository.findById(1L).orElseThrow();

        //then
        assertNotNull(item.getId());
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void itemDelete () {

//        itemService.itemDelete(1L);

    }





    private CreateItemRequest createItemRequest () {
        return CreateItemRequest.builder()
                .name("나우 스몰브리드 퍼피")
                .description("test description")
                .price(39000)
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

//        OptionGroupRequest testOptionGroup2 = OptionGroupRequest.builder()
//                .name("나이별 사료")
//                .exclusive(false)
//                .basic(true)
//                .optionSpecsRequest(createOptionSpecsAge())
//                .build();

        return List.of(testOptionGroup1);
        // java 9 부터 제공되는 불변 리스트를 생성하는 메서드
        // ArratList 와 다르게 리스트에 요소를 추가하거나 삭제할 수 없다.
    }

    private List<OptionSpecsRequest> createOptionSpecsVolume () {
        OptionSpecsRequest testOption1 = OptionSpecsRequest.builder()
                .name("2.5kg")
                .price(5000)
                .build();

        OptionSpecsRequest testOption2 = OptionSpecsRequest.builder()
                .name("5kg")
                .price(10000)
                .build();

        OptionSpecsRequest testOption3 = OptionSpecsRequest.builder()
                .name("7,5")
                .price(20000)
                .build();

        return List.of(testOption1, testOption2, testOption3);
    }
//
//    private List<OptionSpecsRequest> createOptionSpecsAge () {
//        OptionSpecsRequest testOption1 = OptionSpecsRequest.builder()
//                .name("퍼피")
//                .price(3000)
//                .build();
//
//        OptionSpecsRequest testOption2 = OptionSpecsRequest.builder()
//                .name("어덜트")
//                .price(6000)
//                .build();
//
//        OptionSpecsRequest testOption3 = OptionSpecsRequest.builder()
//                .name("시니어")
//                .price(9000)
//                .build();
//
//        return List.of(testOption1, testOption2, testOption3);
//    }

    private ItemEditRequest editItemRequest () {
        return ItemEditRequest.builder()
                .name("test item edit")
                .description("test edit description")
                .price(8900)
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
                .price(9800)
                .id(1L)
                .build();
        return List.of(testEditOption);
    }

}