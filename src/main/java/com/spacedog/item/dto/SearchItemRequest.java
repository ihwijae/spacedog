package com.spacedog.item.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchItemRequest {


    private String searchName;
    private String searchContent;


}
