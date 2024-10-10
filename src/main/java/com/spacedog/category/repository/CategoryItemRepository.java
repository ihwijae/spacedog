package com.spacedog.category.repository;

import com.spacedog.category.domain.CategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {

    List<CategoryItem> findByItemId(Long itemId);
}
