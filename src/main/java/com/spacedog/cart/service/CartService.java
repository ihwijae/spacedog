package com.spacedog.cart.service;

import com.spacedog.cart.domain.CartItem;
import com.spacedog.cart.dto.*;
import com.spacedog.cart.exception.CartException;
import com.spacedog.cart.repository.CartItemRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.repository.ItemJpaRepository;
import com.spacedog.item.repository.ItemRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final ItemRepositoryPort itemRepository;
    private final CartItemRepository cartItemRepository;


    @Transactional
    public Long cartAddItems(CartAddRequest request) {


        log.info("request ={}", request.toString());


        //cart 아이템 등록을 위한 item 엔티티 select
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotEnoughStockException.ItemNotFound("item not found"));

        CartItem cartItem;

        // 상품에 옵션이 있는 경우 장바구니 담기 요청
        if(request.getOptionSpecsIds() != null && !request.getOptionSpecsIds().isEmpty()) {
           // 이미 장바구니에 있는 상품과 동일한 옵션이면 수량만 추가
            log.info("옵션존재");
            if(cartItemRepository.existByItemWithOptions(request.getItemId(), request.getOptionSpecsIds())) {
                log.info("중복 상품 존재함");
               cartItem = cartItemRepository.findCartItems(request.getItemId(), request.getOptionSpecsIds())
                       .orElseThrow(() -> new CartException("장바구니 아이템과 옵션을 불러올 수 없습니다"));
               cartItem.addQuantity(request.getQuantity());
           } else {
               // 없다면 새로 생성
               log.info("장바구니 아이템 새로 생성");
               cartItem = CartItem.createItem(request, item);
               // 중복 옵션 저장믈 막기위한 코드
               request.getOptionSpecsIds().stream()
                       .filter(optionSpecsId -> !cartItem.getOptionSpecsIds().contains(optionSpecsId))
                       .forEach(optionSpecsId -> cartItem.addOptionSpecsId(optionSpecsId));
           }
        }

        // 옵션이 없는 상품을 장바구니 담기 요청
        else {
            log.info("옵션 xx");
            // 이미 장바구니에 있는 상품이면 수량 추가
            if(cartItemRepository.existByItem(request.getItemId())) {
                cartItem = cartItemRepository.findCartItemsWithNotOptions(request.getItemId())
                        .orElseThrow(() -> new CartException("장바구니 아이템을 불러올 수 없습니다"));
                cartItem.addQuantity(request.getQuantity());
            } else {
                cartItem = CartItem.createItem(request, item);
            }
        }

        CartItem save = cartItemRepository.save(cartItem);
        return save.getId();
    }

    // 장바구니 조회
    @Transactional(readOnly = true)
    public CartResponse getCart(Long cartId) {

        /**
         * N+1 문제 해결
         */
        CartResponse cartResponse = CartResponse.builder()
                .totalPrice(0)
                .totalItems(0)
                .build();

        // 루트 조회
        List<ItemCartResponse> itemCartDetail = cartItemRepository.findItemCartDetail(cartId);


        // 컬렉션 한번에 조회
        Map<Long, List<CartOptionResponse>> cartOptionMap = cartItemRepository.findCartSelectOptionMap(cartItemRepository.toCartItemIds(itemCartDetail));
        log.info("cartOptioMap = {}", cartOptionMap);
//        Map<Long, List<String>> cartOptionNameMap = queryRepository.findCartOptionName(queryRepository.toCartItemIds(itemCartDetail));

        // 아이템 ID 목록 추출
        List<Long> itemIds = itemCartDetail.stream()
                .map(ItemCartResponse::getItemId)
                .collect(Collectors.toList());
        log.info("itemIds = {}", itemIds);

        // 아이템 정보를 한번에 조회
        Map<Long, List<Item>> itemMap = cartItemRepository.findItemMap(itemIds);


        // 루프를 돌면 컬렉션 추가 (추가 쿼리x)
        itemCartDetail
                .forEach(itemCartResponse -> {
                    List<CartOptionResponse> cartOptionResponses = cartOptionMap.getOrDefault(itemCartResponse.getId(), Collections.emptyList());
//                    List<String> optionNames = cartOptionNameMap.get(itemCartResponse.getId());


                    cartResponse.setCartItems(itemCartDetail);


                    log.info("cartOptionResponses = {}", cartOptionResponses);
                    itemCartResponse.setSelectedOptions(cartOptionResponses);
//                    itemCartResponse.setOptionName(optionNames);

                    // 미리 조회한 아이템 정보 가져오기
                    List<Item> items = itemMap.get(itemCartResponse.getItemId());

                    int itemTotalPrice = 0;

                    // 기본 상품 가격 계산
                    for (Item item : items) {
                        itemTotalPrice += item.getPrice() * itemCartResponse.getQuantity();
                    }

//                     옵션별 추가 가격 계산
                    for (CartOptionResponse option : cartOptionResponses) {
                        itemTotalPrice += option.getAdditionalPrice() * itemCartResponse.getQuantity();
                    }

                    itemCartResponse.setTotalPrice(itemTotalPrice);

                    //각 상품의 총금액, 갯수 설정
                    cartResponse.setTotalPrice(cartResponse.getTotalPrice() + itemTotalPrice);
                    cartResponse.setTotalItems(cartResponse.getTotalItems() + 1);

                });
        return cartResponse;

        //        List<ItemCartResponse> itemCartDetail = queryRepository.findItemCartDetail(cartId);
//        itemCartDetail
//                .forEach(i -> {
//
//                    List<CartOptionResponse> optionCartDetail = queryRepository.findOptionCartDetail(i.getId());
//                    cartResponse.setCartItems(itemCartDetail);
//
//                    Item item = itemRepository.findById(i.getItemId())
//                            .orElseThrow(() -> new NotEnoughStockException.ItemNotFound("item not found"));
//
//                    // 각 아이템의 총 금액 초기화
//                    int itemTotalPrice = item.getPrice() * i.getQuantity();
//
//                    // 옵션별 추가 가격을 수량에 곱해서 총 가격 계산
//                    for (CartOptionResponse option : optionCartDetail) {
//                        itemTotalPrice += option.getAdditionalPrice() * i.getQuantity();
//                    }
//
//                    // 각 아이템의 총 금액, 갯수 설정
//                    i.setTotalPrice(itemTotalPrice);
//                    i.setOptions(optionCartDetail);
//
//                    // 전체 아이템의 총 금액, 갯수 설정
//                    cartResponse.setTotalItems(cartResponse.getTotalItems() + i.getQuantity());
//                    cartResponse.setTotalPrice(cartResponse.getTotalPrice() + itemTotalPrice);
//                });
//
//        return cartResponse;
    }


    /** 장바구니 수량, 옵션 변경**/
    public void editCartItem(Long cartId, CartEditRequest request) {

    }


}
