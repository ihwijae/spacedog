package com.spacedog.item.service;

import com.spacedog.generic.Money;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.OptionGroupRequest;
import com.spacedog.item.dto.OptionSpecsRequest;
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
        Long resultId = itemService.saveItem(itemRequest);
        Item findItem = itemRepository.findById(resultId).orElseThrow();

        //then
        assertNotNull(resultId); //아이템 id가 null이 아닌지 확인
        Assertions.assertThat(resultId).isEqualTo(findItem.getId());
    }


    private CreateItemRequest createItemRequest () {
        return CreateItemRequest.builder()
                .name("test item")
                .description("test description")
                .categoryId(5)
                .price(Money.wons(10000))
                .stockQuantity(999)
                .optionGroups(createOptionGroup())
                .build();
    }

    private List<OptionGroupRequest> createOptionGroup () {
        OptionGroupRequest testOptionGroup = OptionGroupRequest.builder()
                .name("Test option Group")
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
                .name("Test option")
                .price(Money.wons(20000))
                .build();
        return List.of(testOption);
    }

}