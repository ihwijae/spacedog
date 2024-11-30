package com.spacedog.stock.repository;

import com.spacedog.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByItemIdAndOptionId(Long itemId, Long optionId);
}
