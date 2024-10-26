package com.spacedog.item.service;

import com.spacedog.cart.repository.CartRepository;
import com.spacedog.category.service.CategoryService;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.*;
import com.spacedog.item.repository.ItemRepository;
import com.spacedog.item.repository.ItemRepositoryPort;
import com.spacedog.member.domain.Member;
import com.spacedog.member.repository.MemberRepository;
import com.spacedog.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
public class ItemUnitServiceTest {

    private MemberService memberService;
    private ItemService itemService;
    private ItemRepositoryPort itemRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private CartRepository cartRepository;
    private MemberRepository memberRepository;


//    @BeforeEach
//            public void setup() {
//        memberRepository = new MemberRepository();
//        memberService = new MemberService()
//        itemRepository = new MockItemRepository();
//        itemService = new ItemService()
//    }

//    @Mock
//    private ItemService itemService;
//    @Mock
//    private ItemRepository itemRepository;
//    @Mock
//    private CategoryService categoryService;
//    @Mock
//    private MemberService memberService;
//    @Mock
//    private MemberRepository memberRepository;
//
//
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
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


    // mock 없이 test
    void 아이템생성 () {


    }

    static class MockItemRepository implements ItemRepositoryPort {
        @Override
        public Optional<Item> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public Item save(Item item) {
            return null;
        }

        @Override
        public void delete(Long id) {

        }

        @Override
        public void deleteAll(Iterable<Item> items) {

        }

        @Override
        public void update(Long itemId, ItemEditRequest request) {

        }

        @Override
        public Optional<Item> findByName(String name) {
            return Optional.empty();
        }

        @Override
        public boolean existByName(String name) {
            return false;
        }

        @Override
        public List<SearchItemResponse> getItems(SearchItemRequest request) {
            return List.of();
        }

        @Override
        public List<FindItemAllResponse> findItemsAll(int pageNo, int pageSize) {
            return List.of();
        }

        @Override
        public Optional<Item> findByItemWithCategory(Long id) {
            return Optional.empty();
        }

        @Override
        public List<ItemDetailResponse> itemDetail(Long itemId) {
            return List.of();
        }
    }

}
