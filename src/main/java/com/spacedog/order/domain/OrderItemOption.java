package com.spacedog.order.domain;

import com.spacedog.option.domain.OptionSpecification;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "order_item_option")
@Getter
public class OrderItemOption {

    @Id
    @GeneratedValue
    private Long id;

    private Long orderItemId;

    private String optionName;

    private int optionPrice;


    public OrderItemOption() {
    }

    @Builder
    public OrderItemOption(Long id, Long orderItemId, String optionName, int optionPrice) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.optionName = optionName;
        this.optionPrice = optionPrice;
    }

    public static OrderItemOption create(Long orderItemId, OptionSpecification optionSpecification) {
        return OrderItemOption.builder()
                .orderItemId(orderItemId)
                .optionName(optionSpecification.getName())
                .optionPrice(optionSpecification.getAdditionalPrice())
                .build();
    }
}
