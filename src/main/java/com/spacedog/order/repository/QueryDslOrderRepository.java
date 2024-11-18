package com.spacedog.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.item.domain.QItem;
import com.spacedog.option.domain.QOptionSpecification;
import com.spacedog.order.domain.QOrder;
import com.spacedog.order.domain.QOrderItems;
import com.spacedog.order.service.OrderItemResponse;
import com.spacedog.order.service.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spacedog.item.domain.QItem.item;
import static com.spacedog.option.domain.QOptionSpecification.optionSpecification;
import static com.spacedog.order.domain.QOrder.order;
import static com.spacedog.order.domain.QOrderItems.orderItems;

@Repository
@RequiredArgsConstructor
public class QueryDslOrderRepository {

    private final JPAQueryFactory queryFactory;

    public List<OrderResponse> findOrders(Long id) {


        List<OrderResponse> orderResponse = queryFactory
                .select(Projections.fields(OrderResponse.class,
                        order.id.as("orderId"),
                        order.orderNumber,
                        order.orderDate,
                        order.orderStatus.as("status")
                        ))
                .from(order)
                .where(order.customerId.eq(id))
                .fetch();
        return orderResponse;
    }

    public Map<Long, List<OrderItemResponse>> findOrderItems(List<Long> orderIds) {

        List<OrderItemResponse> orderResponses = queryFactory.select(Projections.fields(OrderItemResponse.class,
                        orderItems.order.id.as("orderId"),
                        orderItems.itemId,
                        item.name.as("itemName"),
                        optionSpecification.name.as("optionName"),
                        orderItems.orderCount.as("quantity"),
                        orderItems.orderPrice.as("price")
                ))
                .from(orderItems)
                .join(item).on(orderItems.itemId.eq(item.id))
                .join(optionSpecification).on(orderItems.optionId.eq(optionSpecification.id))
                .where(orderItems.order.id.in(orderIds))
                .fetch();

        Map<Long, List<OrderItemResponse>> resultMap = orderResponses.stream()
                .collect(Collectors.groupingBy(OrderItemResponse::getOrderId));

        return resultMap;
    }



}
