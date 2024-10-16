package com.spacedog.item.dto;

import com.spacedog.category.domain.Category;
import com.spacedog.generic.Money;
import com.spacedog.item.domain.ItemOptionGroupSpecification;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequest {


    @NotBlank(message = "상품의 이름을 입력하세요")
    private String name;

    @NotBlank(message = "상품의 설명을 입력하세요")
    private String description;

    @NotBlank(message = "상품의 가격을 입력하세요")
    private int price;

    @NotBlank(message = "상품의 재고를 입력하세요")
    private int stockQuantity;

    @NotBlank(message = "상품의 카테고리를 입력하세요")
    private List<Long> categoryIds;


    @NotBlank(message = "상품의 옵션을 입력하세요")
    private List<OptionGroupRequest> optionGroups;

}
