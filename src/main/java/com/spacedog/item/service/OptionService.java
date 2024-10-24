package com.spacedog.item.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.domain.ItemOptionGroupSpecification;
import com.spacedog.item.domain.ItemOptionSpecification;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.repository.OptionRepository;
import com.spacedog.item.repository.OptionSpecsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;
    private final OptionSpecsRepository optionSpecsRepository;


    public void saveOptionWithItem(CreateItemRequest createItemRequest, Item item) {

        createItemRequest.getOptionGroups().stream()
                .map(optionGroupRequest -> {
                    ItemOptionGroupSpecification itemOptionGroup = ItemOptionGroupSpecification.builder()
                            .name(optionGroupRequest.getName())
                            .exclusive(optionGroupRequest.isExclusive())
                            .basic(optionGroupRequest.isBasic())
                            .item(item)
                            .build();

                    //옵션 그룹 저장
                    ItemOptionGroupSpecification saveItemOption = optionRepository.save(itemOptionGroup);

                    //옵션 사양 추가
                    optionGroupRequest.getOptionSpecsRequest()
                            .forEach(optionSpecsRequest -> {
                                ItemOptionSpecification itemOptionSpecs = ItemOptionSpecification.builder()
                                        .name(optionSpecsRequest.getName())
                                        .additionalPrice(optionSpecsRequest.getPrice())
                                        .optionGroupSpecification(saveItemOption)
                                        .stockQuantity(optionSpecsRequest.getStockQuantity())
                                        .build();
                                optionSpecsRepository.save(itemOptionSpecs);
                            });

                    return saveItemOption;
                })
                .collect(Collectors.toList());
    }
}
