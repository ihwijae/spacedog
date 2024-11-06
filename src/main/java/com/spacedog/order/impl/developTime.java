package com.spacedog.order.impl;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class developTime implements OrderNumberGenerator{

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public String OrderNumberCreate() {
        String date = now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        String uuid = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 8);  // 숫자만 8자리

        return date + uuid;
    }
}
