package com.spacedog.order.impl;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class nowTime implements Time{

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
