package com.spacedog.item.service;

import com.spacedog.cart.repository.CartRepository;
import com.spacedog.category.service.CategoryService;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.repository.ItemRepository;
import com.spacedog.item.repository.ItemRepositoryPort;
import com.spacedog.member.domain.Member;
import com.spacedog.member.repository.MemberRepository;
import com.spacedog.member.service.MemberService;
import com.spacedog.member.service.MemberValidate;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Slf4j
public class ItemUnitServiceTest {

    private ItemRepositoryPort itemRepositoryPort = mock(ItemRepositoryPort.class);
    private MemberService memberService = mock(MemberService.class);
    private CategoryService categoryService = mock(CategoryService.class);
    private OptionService optionService = mock(OptionService.class);
    private ItemService itemService;

    @BeforeEach
            public void setup() {
        itemService = new ItemService(memberService, categoryService, optionService, itemRepositoryPort);
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        TestingAuthenticationToken mockAuthentication = new TestingAuthenticationToken("lhj@naver.com", "12345678");
//        context.setAuthentication(mockAuthentication);
//        SecurityContextHolder.setContext(context);
    }

    @Test
    void 판매자는_상품을_등록할수_있다() {

        //given
        Member member = Member.builder()
                .email("test@email.com")
                .id(1L)
                .build();

        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .id(3L)
                .name("testItem")
                .price(9999)
                .build();


        when(memberService.getMember()).thenReturn(member);
        when(itemRepositoryPort.existByName(any())).thenReturn(false);
        when(itemRepositoryPort.save(any(Item.class))).thenReturn(Item.builder().id(3L).name("saveTestItem").build());


        //when
        Long saveId = itemService.createItem(createItemRequest);


        //then
        assertThat(saveId).isEqualTo(3L);
    }

    @Test
    void 상품이_중복되면_등록시_예외가_발생한다() {

        //given
        Member member = Member.builder()
                .email("test@email.com")
                .id(1L)
                .build();

        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .id(3L)
                .name("testItem")
                .price(9999)
                .build();



        when(memberService.getMember()).thenReturn(member);
        when(itemRepositoryPort.existByName(any())).thenReturn(true);
        when(itemRepositoryPort.save(any(Item.class))).thenReturn(Item.builder().id(3L).build());


        //when then
      assertThrows(NotEnoughStockException.ItemDuplicate.class, () -> {
          itemService.createItem(createItemRequest);
      });


    }

    @Test
    void 상품엔티티에서_상품을_생성할수있다() {

        //given
        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .name("testItem")
                .price(9999)
                .build();

        //when
        Item item = Item.createItem(createItemRequest, false);

        //then
        assertThat(item.getName()).isEqualTo("testItem");
    }

    @Test
    void 옵션이_있으면_옵션을_저장하는_메서드를_실행한다() {

        //given
        Member member = Member.builder()
                .email("test@email.com")
                .id(1L)
                .build();



        OptionSpecsRequest testOptionSpecs = OptionSpecsRequest.builder()
                .name("testOptionSpecs")
                .stockQuantity(190)
                .build();

        OptionGroupRequest testOptionGroup = OptionGroupRequest.builder()
                .name("testOptionGroup")
                .optionSpecsRequest(List.of(testOptionSpecs))
                .build();

        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .id(2L)
                .name("testItem")
                .price(9999)
                .optionGroups(List.of(testOptionGroup))
                .build();


        //when
        given(memberService.getMember()).willReturn(member);
        given(itemRepositoryPort.existByName(any())).willReturn(false);
        given(itemRepositoryPort.save(any(Item.class))).willReturn(Item.builder().id(createItemRequest.getId()).build());
        itemService.createItem(createItemRequest);


        // then
        verify(optionService, times(1)).saveOptionWithItem(any(CreateItemRequest.class), any(Item.class));

    }



//
//
//    @Test
//    @DisplayName("아이템 생성")
//    void createItem() {
//
//        //given
//        Member mockMember = Member.builder()
//                .id(1L)
//                .name("testMember")
//                .email("test@example.com")
//                .build();
//
//        // SecurityContext 설정
//        Authentication authentication = new UsernamePasswordAuthenticationToken(mockMember.getEmail(), null);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        when(memberService.getMember()).thenReturn(mockMember);
//        when(memberRepository.findByEmail(mockMember.getEmail())).thenReturn(Optional.of(mockMember));
//
//        Item mockItem = Item.builder()
//                .id(1L)
//                .name("testItem")
//                .build();
//
//         when(itemRepository.save(any(Item.class))).thenReturn(mockItem);
//
//        CreateItemRequest testItemRequest = CreateItemRequest.builder()
//                .name("testItem")
//                .description("testDescription")
//                .price(9999)
//                .build();
//
//        Long itemId = itemService.createItem(testItemRequest);
//
//
//
//        Assertions.assertThat(itemId).isEqualTo(1L);
////        verify(memberService).getMember();
////        verify(itemRepository).save(ArgumentMatchers.any(Item.class));
//
//
//    }






}
