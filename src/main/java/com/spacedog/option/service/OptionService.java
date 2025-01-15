package com.spacedog.option.service;

import com.spacedog.item.domain.Item;
import com.spacedog.option.domain.OptionGroupSpecification;
import com.spacedog.option.domain.OptionSpecification;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.ItemEditRequest;
import com.spacedog.option.repository.OptionRepository;
import com.spacedog.option.repository.OptionSpecsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;
    private final OptionSpecsRepository optionSpecsRepository;


    @Transactional
    public void saveOptionWithItem(CreateItemRequest createItemRequest, Item item) {

        List<OptionGroupSpecification> collect = createItemRequest.getOptionGroups().stream()
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
                                optionSpecsRepository.save(itemOptionSpecs);

                            });

                    return saveItemOption;
                })
                .collect(Collectors.toList());
    }

//    @Transactional
//    public void saveDefaultOptionWithItem(CreateItemRequest createItemRequest, Item item) {
//        ItemOptionGroupSpecification defaultOption = ItemOptionGroupSpecification.builder()
//                .name(item.getName())
//                .exclusive(true)
//                .basic(true)
//                .item(item)
//                .build();
//
//        optionRepository.save(defaultOption);
//
//        ItemOptionSpecification.builder()
//                .name("")
//                .additionalPrice(0)
//                .optionGroupSpecification(defaultOption)
//                .stockQuantity()
//    }

    @Transactional
    public void deleteOptionWithItem(Long itemId) {

        List<OptionGroupSpecification> optionGroup = optionRepository.findByItemId(itemId);

        optionGroup
                .forEach(o -> {
                    List<OptionSpecification> optionSpecs = optionSpecsRepository.findByOptionGroupSpecification_Id(o.getId());
                    optionSpecsRepository.deleteAll(optionSpecs);
                });

        optionRepository.deleteAll(optionGroup);
    }



    @Transactional
    public void editOptionWithItem(ItemEditRequest request, Item item) {

        // 기존 옵션 그룹을 가져와서
        List<OptionGroupSpecification> findOptionGroup = optionRepository.findByItemId(item.getId());

        request.getItemOption()
                .forEach(editOptionGroupRequest -> {
                    //존재하는 옵션그룹이면 업데이트, 아니면 추가
                    OptionGroupSpecification optionGroupSpecification = findOptionGroup.stream()
                            .filter(group -> group.getId().equals(editOptionGroupRequest.getId()))
                            .findFirst()
                            .orElseGet(() -> OptionGroupSpecification.builder()
                                    .name(editOptionGroupRequest.getName())
                                    .exclusive(editOptionGroupRequest.isExclusive())
                                    .basic(editOptionGroupRequest.isBasic())
                                    .item(item)
                                    .build());

                    optionGroupSpecification.update(editOptionGroupRequest);
                    optionRepository.save(optionGroupSpecification);

                    //옵션 스펙 업데이트
                    editOptionGroupRequest.getOptionSpecsRequest()
                            .forEach(editOptionSpecsRequest -> {
                                List<OptionSpecification> optionSpecsList = optionSpecsRepository.findByOptionGroupSpecification_Id(optionGroupSpecification.getId());
                                OptionSpecification optionSpecification = optionSpecsList.stream()
                                        .filter(spec -> spec.getId().equals(editOptionSpecsRequest.getId()))
                                        .findFirst()
                                        .orElseGet(() -> OptionSpecification.builder()
                                                .name(editOptionSpecsRequest.getName())
                                                .additionalPrice(editOptionSpecsRequest.getPrice())
                                                .optionGroupSpecification(optionGroupSpecification)
                                                .build()
                                        );
                                optionSpecification.update(editOptionSpecsRequest, optionGroupSpecification);
                                optionSpecsRepository.save(optionSpecification);
                            });
                });
    }


}
