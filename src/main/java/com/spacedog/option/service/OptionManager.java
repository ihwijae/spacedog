package com.spacedog.option.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.OptionGroupRequest;
import com.spacedog.item.dto.OptionSpecsRequest;
import com.spacedog.option.domain.OptionGroupSpecification;
import com.spacedog.option.domain.OptionSpecification;
import com.spacedog.option.repository.OptionRepository;
import com.spacedog.option.repository.OptionSpecsRepository;
import com.spacedog.stock.service.StockDomainManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OptionManager {

    private final OptionRepository optionRepository;
    private final OptionSpecsRepository optionSpecsRepository;
    private final StockDomainManager stockDomainManager;


    @Transactional
    public Long saveDefaultOption(Item item) {

        OptionGroupSpecification defaultGroup = OptionGroupSpecification.builder()
                .name("defaultGroup")
                .item(item)
                .build();

        OptionGroupSpecification saveOptionGroup = optionRepository.save(defaultGroup);

        OptionSpecification defaultOption = OptionSpecification.builder()
                .name("defaultOption")
                .optionGroupSpecification(saveOptionGroup)
                .build();

        OptionSpecification saveOption = optionSpecsRepository.save(defaultOption);

        return saveOption.getId();

    }

    public void saveOption(Item item, CreateItemRequest createItemRequest) {

        createItemRequest.getOptionGroups().stream()
                .map(optionGroupRequest -> {
                    OptionGroupSpecification itemOptionGroup = OptionGroupSpecification.builder()
                            .name(optionGroupRequest.getName())
                            .exclusive(optionGroupRequest.isExclusive())
                            .basic(optionGroupRequest.isBasic())
                            .item(item)
                            .build();

                    //옵션 그룹 저장
                    OptionGroupSpecification saveItemOption = optionRepository.save(itemOptionGroup);

                    //옵션 사양 추가
                    optionGroupRequest.getOptionSpecsRequest()
                            .forEach(optionSpecsRequest -> {
                                OptionSpecification itemOptionSpecs = OptionSpecification.builder()
                                        .name(optionSpecsRequest.getName())
                                        .additionalPrice(optionSpecsRequest.getPrice())
                                        .optionGroupSpecification(saveItemOption)
                                        .build();
                                OptionSpecification saveOption = optionSpecsRepository.save(itemOptionSpecs);

                                stockDomainManager.createExistOptionItemStock(item.getId(), saveOption.getId(), optionSpecsRequest.getStockQuantity());

                            });

                    return saveItemOption;
                })
                .collect(Collectors.toList());

    }




    public List<OptionSpecification> saveOptions(Item item, CreateItemRequest createItemRequest) {
        return createItemRequest.getOptionGroups().stream()
                .flatMap(optionGroupRequest -> {
                    // 옵션 그룹 생성
                    OptionGroupSpecification itemOptionGroup = OptionGroupSpecification.builder()
                            .name(optionGroupRequest.getName())
                            .exclusive(optionGroupRequest.isExclusive())
                            .basic(optionGroupRequest.isBasic())
                            .item(item)
                            .build();

                    // 옵션 그룹 저장
                    OptionGroupSpecification saveItemOption = optionRepository.save(itemOptionGroup);

                    // 옵션 사양 추가 및 반환
                    return optionGroupRequest.getOptionSpecsRequest().stream()
                            .map(optionSpecsRequest -> {
                                OptionSpecification itemOptionSpecs = OptionSpecification.builder()
                                        .name(optionSpecsRequest.getName())
                                        .additionalPrice(optionSpecsRequest.getPrice())
                                        .optionGroupSpecification(saveItemOption)
                                        .build();

                                // 옵션 사양 저장
                                return optionSpecsRepository.save(itemOptionSpecs);
                            });
                })
                .collect(Collectors.toList());
    }


    public Long saveOptionSpecs(OptionGroupSpecification optionGroup, OptionSpecsRequest optionSpecsRequest) {

        OptionSpecification optionSpecification = OptionSpecification.create(optionSpecsRequest, optionGroup);

        OptionSpecification saveOptionSpecs = optionSpecsRepository.save(optionSpecification);

        return saveOptionSpecs.getId();


    }

    public void deleteOption(List<OptionGroupSpecification> optionGroups) {
        optionRepository.deleteAllInBatch(optionGroups);
    }

    public void deleteOptionSpecs(List<OptionSpecification> optionSpecifications) {
        optionSpecsRepository.deleteAllInBatch(optionSpecifications);
    }

    public void editOption() {

    }



}
