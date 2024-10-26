package com.spacedog.item.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.domain.ItemOptionGroupSpecification;
import com.spacedog.item.domain.ItemOptionSpecification;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.ItemEditRequest;
import com.spacedog.item.repository.OptionRepository;
import com.spacedog.item.repository.OptionSpecsRepository;
import lombok.RequiredArgsConstructor;
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

        List<ItemOptionGroupSpecification> optionGroup = optionRepository.findByItemId(itemId);

        optionGroup
                .forEach(o -> {
                    List<ItemOptionSpecification> optionSpecs = optionSpecsRepository.findByOptionGroupSpecification_Id(o.getId());
                    optionSpecsRepository.deleteAll(optionSpecs);
                });

        optionRepository.deleteAll(optionGroup);
    }

    @Transactional
    public void editOptionWithItem(ItemEditRequest request, Item item) {

        // 기존 옵션 그룹을 가져와서
        List<ItemOptionGroupSpecification> findOptionGroup = optionRepository.findByItemId(item.getId());

        request.getItemOption()
                .forEach(editOptionGroupRequest -> {
                    //존재하는 옵션그룹이면 업데이트, 아니면 추가
                    ItemOptionGroupSpecification itemOptionGroupSpecification = findOptionGroup.stream()
                            .filter(group -> group.getId().equals(editOptionGroupRequest.getId()))
                            .findFirst()
                            .orElseGet(() -> ItemOptionGroupSpecification.builder()
                                    .name(editOptionGroupRequest.getName())
                                    .exclusive(editOptionGroupRequest.isExclusive())
                                    .basic(editOptionGroupRequest.isBasic())
                                    .item(item)
                                    .build());

                    itemOptionGroupSpecification.update(editOptionGroupRequest);
                    optionRepository.save(itemOptionGroupSpecification);

                    //옵션 스펙 업데이트
                    editOptionGroupRequest.getOptionSpecsRequest()
                            .forEach(editOptionSpecsRequest -> {
                                List<ItemOptionSpecification> optionSpecsList = optionSpecsRepository.findByOptionGroupSpecification_Id(itemOptionGroupSpecification.getId());
                                ItemOptionSpecification itemOptionSpecification = optionSpecsList.stream()
                                        .filter(spec -> spec.getId().equals(editOptionSpecsRequest.getId()))
                                        .findFirst()
                                        .orElseGet(() -> ItemOptionSpecification.builder()
                                                .name(editOptionSpecsRequest.getName())
                                                .additionalPrice(editOptionSpecsRequest.getPrice())
                                                .optionGroupSpecification(itemOptionGroupSpecification)
                                                .stockQuantity(editOptionSpecsRequest.getQuantity())
                                                .build()
                                        );
                                itemOptionSpecification.update(editOptionSpecsRequest, itemOptionGroupSpecification);
                                optionSpecsRepository.save(itemOptionSpecification);
                            });
                });
    }


}
