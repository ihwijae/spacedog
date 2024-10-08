package com.spacedog.item.repository;

import com.spacedog.item.domain.ItemOptionSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionSpecsRepository extends JpaRepository<ItemOptionSpecification, Long> {
}
