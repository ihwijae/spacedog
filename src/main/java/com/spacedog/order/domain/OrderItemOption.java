package com.spacedog.order.domain;

import com.spacedog.option.domain.OptionSpecification;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "order_item_option")
@Getter
public class OrderItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderItemId;

    private String optionName;

    private int optionPrice;

    private Long optionId;


    public OrderItemOption() {
    }

    @Builder
    public OrderItemOption(Long id, Long orderItemId, String optionName, int optionPrice, Long optionId) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.optionName = optionName;
        this.optionPrice = optionPrice;
        this.optionId = optionId;
    }

    public static List<OrderItemOption> create(List<OrderItems> orderItems, List<OptionSpecification> optionSpecification) {


        List<OrderItemOption> orderItemOptions = orderItems.stream()
                .flatMap(orderItem -> optionSpecification.stream()
                        .map(optionSpec -> OrderItemOption.builder()
                                .orderItemId(orderItem.getId())
                                .optionId(optionSpec.getId())
                                        .optionName(optionSpec.getName())
                                        .optionPrice(optionSpec.getAdditionalPrice())
                                        .build()
                                )
                )
                .collect(Collectors.toList());

        return orderItemOptions;
    }
}
