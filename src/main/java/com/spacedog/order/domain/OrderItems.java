package com.spacedog.order.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "order_item")
@Getter
public class OrderItems {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;


    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "option_id")
    private Long optionId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;


    @Column(name = "order_price")
    private int orderPrice;

    @Column(name = "order_count")
    private int orderCount;


    @Builder
    public OrderItems(Long id, Long itemId, Order order, int orderPrice, int orderCount, Long optionId) {
        this.id = id;
        this.itemId = itemId;
        this.order = order;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
        this.optionId = optionId;
    }

    public OrderItems() {
    }

    public static OrderItems createOrderItem(Long itemId, Order order, int orderPrice, int orderCount, Long optionId) {
        return OrderItems.builder()
                .itemId(itemId)
                .order(order)
                .orderPrice(orderPrice)
                .orderCount(orderCount)
                .optionId(optionId)
                .build();
    }
}
