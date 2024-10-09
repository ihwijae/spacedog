package com.spacedog.item.repository;

import com.spacedog.item.domain.ItemOptionSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionSpecsRepository extends JpaRepository<ItemOptionSpecification, Long> {

    Optional<ItemOptionSpecification> findByOptionGroupSpecification_Id(Long id);
}
