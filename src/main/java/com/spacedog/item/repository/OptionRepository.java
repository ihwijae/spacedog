package com.spacedog.item.repository;

import com.spacedog.item.domain.ItemOptionGroupSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<ItemOptionGroupSpecification, Long> {
}
