package com.spacedog.delivery.repository;

import com.spacedog.delivery.domain.Delivery;

import java.util.Optional;

public interface DeliveryRepository {

    Delivery save(Delivery delivery);
    Optional<Delivery> findById(Long id);
}
