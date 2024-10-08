package com.spacedog.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class itemOptionGroupResponse {


    private String name;
    private boolean exclusive;
    private boolean basic;

    private List<ItemOptionSpecsResponse> optionSpecs;
}
