package com.spacedog.item.repository;

import com.spacedog.item.domain.ItemOptionGroupSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<ItemOptionGroupSpecification, Long> {

}
