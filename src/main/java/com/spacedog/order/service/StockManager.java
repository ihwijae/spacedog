package com.spacedog.order.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.impl.ItemReader;
import com.spacedog.item.repository.ItemRepositoryPort;
import com.spacedog.option.domain.OptionSpecification;
import com.spacedog.option.repository.OptionSpecsRepository;
import com.spacedog.order.impl.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockManager {

    private final ItemRepositoryPort itemRepository;
    private final OptionSpecsRepository optionSpecsRepository;
    private final ItemReader itemReader;

    public void checkQuantity(OrderCreateRequest request) {



        request.getOrderItemCreate()
                .forEach(orderItem -> {

                    if(orderItem.getOptionId() != null ) {
                        OptionSpecification option = optionSpecsRepository.findById(orderItem.getOptionId())
                                .orElseThrow(() -> new NotEnoughStockException("옵션을 찾을 수 없습니다"));

                        option.checkQuantity(orderItem.getAmount());

                    } else {
                        Item item = itemRepository.findById(orderItem.getItemId())
                                .orElseThrow(() -> new NotEnoughStockException.ItemNotFound("상품을 찾을 수 없습니다"));

                        item.checkQuantity(orderItem.getAmount());
                    }
                });

    }

    public void stockDecrease(OrderCreateRequest request) {


        request.getOrderItemCreate()
                .forEach(orderItem -> {
                    Item item = itemReader.findById(orderItem.getItemId());

                    if(orderItem.getOptionId() != null ) {
                        OptionSpecification option = optionSpecsRepository.findById(orderItem.getOptionId())
                                .orElseThrow(() -> new OrderException("상품 옵션을 불러올 수 없습니다"));

                        option.removeQuantity(orderItem.getAmount(), item);
                    } else {

                        item.removeStock(orderItem.getAmount());
                    }
                });
    }

}
