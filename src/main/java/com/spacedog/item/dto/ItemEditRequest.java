package com.spacedog.item.dto;

import com.spacedog.generic.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEditRequest {



    private String name;
    private String description;
    private Money price;
    private int stockQuantity;
    private Long categoryId;
    private List<OptionGroupRequest> itemOption = new ArrayList<>();
}
