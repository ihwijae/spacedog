package com.spacedog.order.dto;

import com.spacedog.member.domain.Address;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDetailResponse {

    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDate;

    private String receiver;
    private String receiverPhone;
    private Address receiverAddress;
    private String deliveryRequest;

}
