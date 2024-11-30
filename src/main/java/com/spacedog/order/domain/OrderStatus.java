package com.spacedog.order.domain;

public enum OrderStatus {
    PENDING,         // 대기 중
    CONFIRMED,          // 결제 확인
    PROCESSING,      // 준비 중
    SHIPPED,         // 배송 중
    DELIVERED,       // 배송 완료
    CANCELLED,       // 취소됨
    RETURNED,        // 반품됨
    COMPLETED,       // 완료됨
    CANCEL

}
