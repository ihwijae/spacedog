package com.spacedog.cart.service;

import com.spacedog.cart.domain.Cart;
import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.dto.CartAddRequest;
import com.spacedog.cart.exception.CartException;
import com.spacedog.cart.repository.CartItemQueryRepository;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.cart.repository.CartRepository;
import com.spacedog.generic.Money;
import com.spacedog.item.domain.Item;
import com.spacedog.item.domain.ItemOptionSpecification;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.repository.ItemQueryRepository;
import com.spacedog.item.repository.ItemRepository;
import com.spacedog.item.repository.OptionSpecsRepository;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

//        Optional<CartItem> findCartItem = cartItemRepository.findByItemIdAndOptionSpecsIds(request.getItemId(), request.getOptionSpecsIds());
        Boolean exist = queryRepository.exist(request.getItemId(), request.getOptionSpecsIds());

        // 장바구니 아이템이 이미 존재하는 상품이면 수량만 추가
//        if(!findCartItem.isEmpty()) {
//            log.info("findCartItem = {}", findCartItem);
////            findCartItem.forEach(cartItem -> {
////                cartItem.addQuantity(request.getQuantity());
////                cartItemRepository.save(cartItem);
////            });
//            CartItem cartItem = findCartItem.get();
//            cartItem.addQuantity(request.getQuantity());
//        }
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

        // 장바구니에 총금액, 총 상품 갯수 설정
        int totalPrice = 0;
        int totalItems = 0;

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotEnoughStockException.ItemNotFound("item not found"));



        /** forEach 문을 사용하지 않은 이유는 forEach문은 메서드 안에서 지역변수 수정이 불가능하다.
         * 정확히는 가능하지만 복잡하고, 직관적이지 않아서 for 문을사용
         * **/
        for (Long optionSpecsId : request.getOptionSpecsIds()) {
            if(!cartItem.getOptionSpecsIds().contains(optionSpecsId)) {
                ItemOptionSpecification itemOptionSpecification = optionSpecsRepository.findById(optionSpecsId)
                        .orElseThrow(() -> new NotEnoughStockException.OptionSpecsNotFound("options not found"));
                // 옵션 id 추가
                cartItem.addOptionSpecsId(optionSpecsId);

                // 추가 가격 계산
                totalPrice += itemOptionSpecification.getAdditionalPrice();
            }
        }

        CartItem save = cartItemRepository.save(cartItem);

        int itemTotalPrice = item.getPrice() + totalPrice;
        itemTotalPrice *= cartItem.getQuantity();

        totalItems += cartItem.getQuantity();

        cart.updateTotalPrice(itemTotalPrice);
        cart.updateTotalItems(totalItems);
        cartRepository.save(cart);

        return save.getId();
    }
}
