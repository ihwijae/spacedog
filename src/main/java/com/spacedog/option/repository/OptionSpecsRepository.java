package com.spacedog.option.repository;

import com.spacedog.option.domain.OptionSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionSpecsRepository extends JpaRepository<OptionSpecification, Long> {

    List<OptionSpecification> findByOptionGroupSpecification_Id(Long id);
    List<OptionSpecification> findByIdIn(List<Long> optionIds);
}
