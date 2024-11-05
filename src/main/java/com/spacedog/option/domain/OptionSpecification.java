package com.spacedog.option.domain;

import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.EditOptionSpecsRequest;
import com.spacedog.item.exception.NotEnoughStockException;
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
public class OptionSpecification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_specs_id")
    private Long id;


    private String name;

    @Column(name = "additional_amount")
//    @Convert(converter = MoneyConverter.class)
    private int additionalPrice; //옵션에 따른 추가금액 0일수도 아닐수도

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_spec_id")
    private OptionGroupSpecification optionGroupSpecification;




    public void update (EditOptionSpecsRequest request, OptionGroupSpecification optionGroupSpec) {
        this.name = request.getName();
        this.additionalPrice = request.getPrice();
        this.optionGroupSpecification = optionGroupSpec;
    }


    public void addQuantity(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeQuantity(int quantity, Item item) {
        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            throw new NotEnoughStockException("재고가 없습니다");
        }

        this.stockQuantity = restStock;


        item.removeStock(quantity);
    }



}
