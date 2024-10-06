package com.spacedog.item.domain;

import com.spacedog.generic.Money;
import com.spacedog.generic.MoneyConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "option_specs")
public class ItemOptionSpecification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_specs_id")
    private Long id;


    private String name;

    @Column(name = "price")
    private Money price;

    @Column(name = "option_group_spec_id")
    private Long optionGroupSpecId;




}
