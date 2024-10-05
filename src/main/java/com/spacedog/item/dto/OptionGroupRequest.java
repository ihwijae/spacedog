package com.spacedog.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionGroupRequest {


    private String name;

    private boolean exclusive; // 추가된 필드
    private boolean basic; // 추가된 필드

    private List<OptionSpecsRequest> optionSpecsRequest = new ArrayList<>();
}
