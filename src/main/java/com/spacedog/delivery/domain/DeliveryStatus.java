package com.spacedog.delivery.domain;

public enum DeliveryStatus {
    PENDING,     // 배송 대기 중
    SHIPPED,     // 배송 시작됨
    IN_TRANSIT,  // 배송 중
    DELIVERED,   // 배송 완료됨
    CANCELED,    // 배송 취소됨
    RETURNED;    // 반품됨
}
