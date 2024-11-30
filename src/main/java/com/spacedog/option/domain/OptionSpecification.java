package com.spacedog.option.domain;

import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.EditOptionSpecsRequest;
import com.spacedog.item.dto.OptionSpecsRequest;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.order.service.OrderException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "option_specs")
public class OptionSpecification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_specs_id")
    private Long id;


    private String name;

    @Column(name = "additional_price")
//    @Convert(converter = MoneyConverter.class)
    private int additionalPrice; //옵션에 따른 추가금액 0일수도 아닐수도



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_spec_id")
    private OptionGroupSpecification optionGroupSpecification;




    public void update (EditOptionSpecsRequest request, OptionGroupSpecification optionGroupSpec) {
        this.name = request.getName();
        this.additionalPrice = request.getPrice();
        this.optionGroupSpecification = optionGroupSpec;
    }

    public OptionSpecification addOptionGroupSpecification (List<OptionGroupSpecification> optionGroupSpec) {

        optionGroupSpec.forEach(optionGroupSpecification -> {
            this.optionGroupSpecification = optionGroupSpecification;
        });
        return this;
    }

    public static OptionSpecification create(OptionSpecsRequest request, OptionGroupSpecification optionGroupSpec) {

        return OptionSpecification.builder()
                .name(request.getName())
                .additionalPrice(request.getPrice())
                .optionGroupSpecification(optionGroupSpec)
                .build();
    }











}
