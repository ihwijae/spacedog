package com.spacedog.option.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.OptionGroupRequest;
import com.spacedog.option.domain.OptionGroupSpecification;
import com.spacedog.option.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OptionGroupManager {

    private final OptionRepository optionRepository;



    public OptionGroupSpecification createOptionGroup(Item item, OptionGroupRequest optionGroupRequest) {

        OptionGroupSpecification optionGroupSpecification = OptionGroupSpecification.create(item, optionGroupRequest);

        OptionGroupSpecification saveOptionGroup = optionRepository.save(optionGroupSpecification);

        return saveOptionGroup;
    }
}
