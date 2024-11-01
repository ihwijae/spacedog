package com.spacedog.item.service;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);


//    @Mapping(target = "id" , ignore = true)
////    @Mapping(target = "itemOption", source = "optionGroups")
//    Item toEntity(CreateItemRequest createItemRequest);







}
