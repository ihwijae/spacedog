package com.spacedog.stock.repository;

import com.spacedog.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {
}
