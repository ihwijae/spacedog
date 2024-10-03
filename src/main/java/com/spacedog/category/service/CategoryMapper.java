package com.spacedog.category.service;

import com.spacedog.category.domain.Category;
import com.spacedog.member.service.MemberMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);


    CategoryDto toDto(Category category);
}
