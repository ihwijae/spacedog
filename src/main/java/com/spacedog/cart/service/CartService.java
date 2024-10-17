package com.spacedog.cart.service;

import com.spacedog.cart.domain.Cart;
import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.dto.CartAddRequest;
import com.spacedog.cart.dto.CartResponse;
import com.spacedog.cart.exception.CartException;
import com.spacedog.cart.repository.CartItemQueryRepository;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.cart.repository.CartRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.domain.ItemOptionSpecification;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.repository.ItemRepository;
import com.spacedog.item.repository.OptionSpecsRepository;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.spacedog.cart.exception.CartException.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final CartItemRepository cartItemRepository;
    private final CartItemQueryRepository queryRepository;
    private final CartRepository cartRepository;
    private final OptionSpecsRepository optionSpecsRepository;




    @Transactional
    public Long cartAddItems(CartAddRequest request) {

        Member member = memberService.getMember();

        Cart cart = cartRepository.findById(member.getId())
                .orElseThrow(() -> new NotFoundCartException("장바구니를 불러올 수 없습니다"));

        Boolean exist = queryRepository.exist(request.getItemId(), request.getOptionSpecsIds());


        CartItem cartItem;

        if(exist) {
            log.info("아이템, 옵션 중복이 발생");
            cartItem = queryRepository.findCartItems(request.getItemId(), request.getOptionSpecsIds())
                    .orElseThrow(() -> new CartException("장바구니 아이템을 불러올 수 없습니다"));
            cartItem.addQuantity(request.getQuantity());
        } else {
            // 없다면 새로 생성
            log.info("중복 상품이 없으니 새로운 상품 담기");
            cartItem = CartItem.builder()
                    .itemId(request.getItemId())
                    .quantity(request.getQuantity())
                    .cartId(member.getId())
                    .build();
        }


        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotEnoughStockException.ItemNotFound("item not found"));

        // 중복 옵션 저장믈 막기위한 코드
        request.getOptionSpecsIds().stream()
                .filter(optionSpecsId -> !cartItem.getOptionSpecsIds().contains(optionSpecsId))
                .forEach(optionSpecsId -> cartItem.addOptionSpecsId(optionSpecsId));

        // 장바구니에 총금액, 총 상품 갯수 설정
        // 루프문에서 해당 옵션 스펙의 추가 요금을 담는 변수.
        int totalPrice = 0;
        int totalItems = 0;

        // 추가 가격 계산
        /** forEach 문을 사용하지 않은 이유는 forEach문은 메서드 안에서 지역변수 수정이 불가능하다.
         * 정확히는 가능하지만 복잡하고, 직관적이지 않아서 for 문을사용
         * **/
        for(Long optionSpecsId : request.getOptionSpecsIds()) {
            ItemOptionSpecification itemOptionSpecification = optionSpecsRepository.findById(optionSpecsId)
                    .orElseThrow(() -> new NotEnoughStockException.OptionSpecsNotFound("option specs not found"));

            totalPrice += itemOptionSpecification.getAdditionalPrice();
            log.info("totalPirce = {}", totalPrice);

        }

        CartItem save = cartItemRepository.save(cartItem);

        int itemTotalPrice = item.getPrice() + totalPrice;
        itemTotalPrice *= request.getQuantity();
        log.info("itemTotalPrice: {}", itemTotalPrice);

        totalItems += request.getQuantity();
        log.info("totalItems: {}", totalItems);

        cart.updateTotalPrice(itemTotalPrice);
        cart.updateTotalItems(totalItems);
        cartRepository.save(cart);

        return save.getId();
    }

    // 장바구니 조회
    @Transactional(readOnly = true)
    public List<CartResponse> getCart() {

    }


}
