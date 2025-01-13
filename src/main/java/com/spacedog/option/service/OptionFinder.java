package com.spacedog.option.service;

import com.spacedog.option.domain.OptionGroupSpecification;
import com.spacedog.option.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OptionFinder {

    private final OptionRepository optionRepository;


    public List<OptionGroupSpecification> findOptionGroups(Long itemId) {

      return optionRepository.findByItemId(itemId);
    }

    public List<Long> findOptionGroupIds(List<OptionGroupSpecification> optionGroups) {

        return optionGroups.stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());
    }

    


}
