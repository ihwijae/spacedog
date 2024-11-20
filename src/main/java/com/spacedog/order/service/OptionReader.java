package com.spacedog.order.service;

import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.exception.NotEnoughStockException.OptionSpecsNotFound;
import com.spacedog.option.domain.OptionSpecification;
import com.spacedog.option.repository.OptionSpecsRepository;
import com.spacedog.order.impl.OrderCreateRequest.OrderItemCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OptionReader {

    private final OptionSpecsRepository optionSpecsRepository;


    public List<OptionSpecification> findOptionSpecs(List<OrderItemCreate> orderItemCreates) {


        List<OptionSpecification> optionSpecs = orderItemCreates.stream()
                .filter(orderItemCreate -> orderItemCreate.getOptionId() != null)
                .map(orderItemCreate -> optionSpecsRepository.findById(orderItemCreate.getOptionId())
                        .orElseThrow(() -> new OptionSpecsNotFound("옵션을 찾을 수 없습니다")))
                .collect(Collectors.toList());

       if (!optionSpecs.isEmpty()) {
           return optionSpecs;
       } else {
           return Collections.emptyList(); //빈 리스트 반환
       }
    }


}
