package com.spacedog.item.service;

import com.spacedog.item.domain.Item;
import com.spacedog.item.domain.ItemOptionGroupSpecification;
import com.spacedog.item.domain.ItemOptionSpecification;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.FindItemAllResponse;
import com.spacedog.item.dto.OptionGroupRequest;
import com.spacedog.item.dto.OptionSpecsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);


    @Mapping(target = "id" , ignore = true)
//    @Mapping(target = "itemOption", source = "optionGroups")
    Item toEntity(CreateItemRequest createItemRequest);







    @Mapping(target = "id" , ignore = true)
    List<FindItemAllResponse> toFindItemAllResponse(List<Item> items);






}
