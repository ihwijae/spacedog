package com.spacedog.option.service;

import com.spacedog.option.domain.OptionGroupSpecification;
import com.spacedog.option.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OptionFinder {

    private final OptionRepository optionRepository;


    public List<OptionGroupSpecification> findOption(Long itemId) {

        return optionRepository.findByItemId(itemId);
    }
}
