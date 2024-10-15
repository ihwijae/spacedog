package com.spacedog.order.domain;

import com.spacedog.generic.Money;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;


    @Column(name = "order_price")
    private int orderPrice;

    @Column(name = "order_count")
    private int orderCount;

    @Builder
    public OrderItems(Long id, Long itemId, Order order, int orderPrice, int orderCount) {
        this.id = id;
        this.itemId = itemId;
        this.order = order;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
    }

    public OrderItems() {
    }

    public static OrderItems createOrderItem(Long itemId, Order order, int orderPrice, int orderCount) {
        return OrderItems.builder()
                .itemId(itemId)
                .order(order)
                .orderPrice(orderPrice)
                .orderCount(orderCount)
                .build();
    }
}
