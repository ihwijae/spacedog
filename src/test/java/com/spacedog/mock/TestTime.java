package com.spacedog.mock;

import com.spacedog.order.impl.Time;

import java.time.LocalDateTime;

public class TestTime implements Time {

    private final LocalDateTime currentTime;

    public TestTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    // static 메서드로 특정 시간을 생성하는 메서드
    public static LocalDateTime of(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, day, hour, minute, second);  // 특정 시간 객체를 생성
    }

    @Override
    public LocalDateTime now() {
        return this.currentTime;
    }
}
