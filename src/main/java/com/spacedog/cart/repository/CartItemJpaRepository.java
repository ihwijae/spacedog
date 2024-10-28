package com.spacedog.cart.repository;

import com.spacedog.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemJpaRepository extends JpaRepository<CartItem, Long> {


    @Query("select c from CartItem c join c.optionSpecsIds os where c.itemId = :itemId AND os in :optionSpecsIds")
    boolean existsByItemIdAndOptionSpecsIds(@Param("itemId") Long itemId, @Param("optionSpecsIds") List<Long> optionSpecsIds);
}

