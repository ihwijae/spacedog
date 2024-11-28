package com.spacedog.stock.service;

import com.spacedog.stock.domain.Stock;
import com.spacedog.stock.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockDomainManager {

    private final StockJpaRepository stockJpaRepository;

    public void createNotOptionItemStock(Long itemId, Long optionId, int stockQuantity) {
        Stock stock = Stock.builder()
                .itemId(itemId)
                .optionId(optionId)
                .quantity(stockQuantity)
                .build();

        stockJpaRepository.save(stock);

    }

    public void createExistOptionItemStock(Long itemId,  Long optionId, int stockQuantity) {
        Stock stock = Stock.builder()
                .itemId(itemId)
                .optionId(optionId)
                .quantity(stockQuantity)
                .build();

        stockJpaRepository.save(stock);

    }
}
