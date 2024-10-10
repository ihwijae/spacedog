package com.spacedog.item.repository;

import com.spacedog.item.domain.ItemOptionGroupSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<ItemOptionGroupSpecification, Long> {

    List<ItemOptionGroupSpecification> findByItemId(Long itemId);

}
