package com.spacedog.order.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.impl.ItemReader;
import com.spacedog.option.domain.OptionSpecification;
import com.spacedog.option.repository.OptionSpecsRepository;
import com.spacedog.order.impl.OrderCreateRequest;
import com.spacedog.stock.domain.Stock;
import com.spacedog.stock.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockManager {

    private final OptionSpecsRepository optionSpecsRepository;
    private final ItemReader itemReader;
    private final StockJpaRepository stockJpaRepository;

    public void checkQuantity(OrderCreateRequest request) {

        request.getOrderItemCreate()
                .forEach(orderItem -> {

                    Stock stock = stockJpaRepository.findByItemIdAndOptionId(orderItem.getItemId(), orderItem.getOptionId())
                            .orElseThrow(() -> new IllegalArgumentException("재고 객체를 불러올 수 없습니다"));

                    stock.checkQuantity(orderItem.getAmount());

                });

    }

    public void stockDecrease(OrderCreateRequest request) {


        request.getOrderItemCreate()
                .forEach(orderItem -> {

                    Stock stock = stockJpaRepository.findByItemIdAndOptionId(orderItem.getItemId(), orderItem.getOptionId())
                            .orElseThrow(() -> new IllegalArgumentException("재고 객체를 불러올 수 없습니다"));

                    stock.removeStock(orderItem.getAmount());

                });
    }


    public void orderCancelToStock(Long itemId, Long optionId, int quantity) {

        Stock stock = stockJpaRepository.findByItemIdAndOptionId(itemId, optionId)
                .orElseThrow(() -> new IllegalArgumentException("재고 엔티티를 불러올 수 없습니다"));

        stock.addStock(quantity);

    }


}
