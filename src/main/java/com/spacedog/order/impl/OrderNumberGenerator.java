package com.spacedog.order.impl;

import java.time.LocalDateTime;

public interface OrderNumberGenerator {

    LocalDateTime now();
    String OrderNumberCreate();
}
