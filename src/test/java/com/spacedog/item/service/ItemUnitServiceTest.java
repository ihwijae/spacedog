package com.spacedog.item.service;

import com.spacedog.category.domain.Category;
import com.spacedog.category.exception.CategoryNotFoundException.CategoryOfNot;
import com.spacedog.category.repository.CategoryItemRepository;
import com.spacedog.category.repository.CategoryQueryRepository;
import com.spacedog.category.repository.CategoryRepository;
import com.spacedog.category.service.CategoryService;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;
import com.spacedog.item.exception.NotEnoughStockException;

import com.spacedog.item.repository.ItemRepositoryPort;
import com.spacedog.member.domain.Member;


import com.spacedog.mock.FakeItemRepository;
import com.spacedog.option.service.OptionService;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemUnitServiceTest {

    private OptionService optionService = mock(OptionService.class);
    private ItemService itemService;
//    private ItemRepositoryPort itemRepositoryPort = new FakeItemRepository();
    private ItemRepositoryPort itemRepositoryPort = mock(ItemRepositoryPort.class);


    private CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private CategoryItemRepository categoryItemRepository = mock(CategoryItemRepository.class);
    private CategoryQueryRepository categoryQueryRepository = mock(CategoryQueryRepository.class);
    private CategoryService categoryService;


    @BeforeEach
            public void setup() {
//        categoryService = new CategoryService(categoryRepository,categoryQueryRepository, categoryItemRepository);
//        itemService = new ItemService(categoryService, optionService, itemRepositoryPort);
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
                .categoryIds(List.of(1L, 2L))
                .stockQuantity(300)
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("testItem")
                .description("testDescription")
                .price(999)
                .build();


        when(itemRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemRepositoryPort.existByName(any())).thenReturn(false);
        when(itemRepositoryPort.save(any(Item.class))).thenReturn(Item.builder().id(createItemRequest.getId()).name("saveTestItem").build());
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Category.builder().name("testCategory").build()));




        //when
        Long saveId = itemService.createItem(createItemRequest, member);


        //then
        assertThat(saveId).isEqualTo(3L);

    }

    @Test
    void 상품을등록할때_카테고리가_없으면_예외가발생한다 () {
        //given
        Member member = Member.builder()
                .email("test@email.com")
                .id(1L)
                .build();

        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .id(3L)
                .name("testItem")
                .price(9999)
                .categoryIds(Collections.emptyList())
                .stockQuantity(300)
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("testItem")
                .description("testDescription")
                .price(999)
                .build();


        when(itemRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemRepositoryPort.existByName(any())).thenReturn(false);
        when(itemRepositoryPort.save(any(Item.class))).thenReturn(Item.builder().id(3L).name("saveTestItem").build());


        //when then
        assertThrows(CategoryOfNot.class, () -> {
            itemService.createItem(createItemRequest, member);
        });
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
                .stockQuantity(300)
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("testItem")
                .description("testDescription")
                .price(999)
                .build();



        when(itemRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemRepositoryPort.existByName(any())).thenReturn(true);
        when(itemRepositoryPort.save(any(Item.class))).thenReturn(Item.builder().id(3L).build());


        //when then
      assertThrows(NotEnoughStockException.ItemDuplicate.class, () -> {
          itemService.createItem(createItemRequest,member);
      });


    }


    @Test
    void 옵션이_있으면_옵션을_저장하는_메서드를_실행한다() {

        //given
        Member member = Member.builder()
                .email("test@email.com")
                .id(1L)
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("testItem")
                .description("testDescription")
                .price(999)
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
                .categoryIds(List.of(1L, 2L))
                .stockQuantity(200)
                .build();


        //when
        when(itemRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        given(itemRepositoryPort.existByName(any())).willReturn(false);
        given(itemRepositoryPort.save(any(Item.class))).willReturn(Item.builder().id(createItemRequest.getId()).build());
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Category.builder().name("testCategory").build()));

        itemService.createItem(createItemRequest, member);


         //then
        verify(optionService, times(1)).saveOptionWithItem(any(CreateItemRequest.class), any(Item.class));
    }














}
