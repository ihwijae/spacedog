package com.spacedog.option.repository;

import com.spacedog.option.domain.OptionGroupSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<OptionGroupSpecification, Long> {

    List<OptionGroupSpecification> findByItemId(Long itemId);

}
