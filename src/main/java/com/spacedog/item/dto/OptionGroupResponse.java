package com.spacedog.item.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OptionGroupResponse {



    private String name;

    private boolean exclusive;

    private boolean basic;

    private List<OptionSpecsResponse> optionSpecs;


    @Builder
    public OptionGroupResponse(String name, boolean exclusive, boolean basic, List<OptionSpecsResponse> optionSpecs) {
        this.name = name;
        this.exclusive = exclusive;
        this.basic = basic;
        this.optionSpecs = optionSpecs;
    }

    public OptionGroupResponse() {
    }
}
