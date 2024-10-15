package com.spacedog.item.domain;

import com.spacedog.generic.Money;
import com.spacedog.generic.MoneyConverter;
import com.spacedog.item.dto.EditOptionSpecsRequest;
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
    @Convert(converter = MoneyConverter.class)
    private Money additionalPrice; //옵션에 따른 추가금액 0일수도 아닐수도

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_spec_id")
    private ItemOptionGroupSpecification optionGroupSpecification;



    public void update (EditOptionSpecsRequest request, ItemOptionGroupSpecification optionGroupSpec) {
        this.name = request.getName();
        this.additionalPrice = request.getPrice();
        this.optionGroupSpecification = optionGroupSpec;
    }



}
