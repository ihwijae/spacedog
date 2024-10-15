package com.spacedog.order.service;

import com.spacedog.generic.Money;
import com.spacedog.member.domain.Address;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class OrderCreateRequest {


    private String name;

    private String phone;

    @Embedded
    private Address address;


    private List<OrderItemCreate> orderItemCreate;



    @Builder
    public OrderCreateRequest(String name, String phone, Address address, List<OrderItemCreate> orderItemCreate) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.orderItemCreate = orderItemCreate;
    }

    public OrderCreateRequest() {
    }

    @Getter
    public static class OrderItemCreate {
        private Long itemId;
        private int amount; //상품 수량
        private int orderPrice;

        public OrderItemCreate() {

        }

        @Builder
        public OrderItemCreate(Long itemId, int amount, int orderPrice) {
            this.itemId = itemId;
            this.amount = amount;
            this.orderPrice = orderPrice;
        }
    }
}
