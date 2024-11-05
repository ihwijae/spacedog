package com.spacedog.delivery.impl;

import com.spacedog.delivery.domain.Delivery;
import com.spacedog.delivery.domain.DeliveryStatus;
import com.spacedog.delivery.repository.DeliveryRepository;
import com.spacedog.member.domain.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryWriter {

    private final DeliveryRepository deliveryRepository;

    public Long createDelivery(Address address) {
        Delivery delivery = Delivery.builder()
                .address(address)
                .status(DeliveryStatus.PENDING)
                .build();

        Delivery result = deliveryRepository.save(delivery);
        return result.getId();
    }
}
