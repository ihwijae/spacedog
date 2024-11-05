package com.spacedog.delivery.repository;

import com.spacedog.delivery.domain.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeliveryJpaRepository implements DeliveryRepository {

    private final SpringDataJpaDelivery springDataJpaDelivery;

    @Override
    public Delivery save(Delivery delivery) {
        return springDataJpaDelivery.save(delivery);
    }

    @Override
    public Optional<Delivery> findById(Long id) {
        return springDataJpaDelivery.findById(id);
    }
}
