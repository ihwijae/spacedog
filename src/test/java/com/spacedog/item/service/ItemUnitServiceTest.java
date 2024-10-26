package com.spacedog.item.service;

import com.spacedog.category.service.CategoryService;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.repository.ItemRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
public class ItemUnitServiceTest {

    @Mock
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("아이템 생성")
    void createItem() {

        //given
        Member mockMember = Member.builder()
                .id(1L)
                .name("testMember")
                .email("test@example.com")
                .build();

        // SecurityContext 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockMember.getEmail(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(memberService.getMember()).thenReturn(mockMember);
        when(memberRepository.findByEmail(mockMember.getEmail())).thenReturn(Optional.of(mockMember));

        Item mockItem = Item.builder()
                .id(1L)
                .name("testItem")
                .build();

         when(itemRepository.save(any(Item.class))).thenReturn(mockItem);

        CreateItemRequest testItemRequest = CreateItemRequest.builder()
                .name("testItem")
                .description("testDescription")
                .price(9999)
                .build();

        Long itemId = itemService.createItem(testItemRequest);



        Assertions.assertThat(itemId).isEqualTo(1L);
//        verify(memberService).getMember();
//        verify(itemRepository).save(ArgumentMatchers.any(Item.class));


    }
}
