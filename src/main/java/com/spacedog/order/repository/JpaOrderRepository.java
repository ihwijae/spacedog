package com.spacedog.order.repository;

import com.spacedog.order.domain.Order;
import com.spacedog.order.dto.OrderDetailResponse;
import com.spacedog.order.service.OrderException;
import com.spacedog.order.service.OrderItemResponse;
import com.spacedog.order.service.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataJpaOrderRepository repository;
    private final QueryDslOrderRepository queryDslRepository;


    @Override
    public Order findById(long id) {
        return repository.findById(id).orElseThrow(() -> new OrderException("주문을 불러올 수 없습니다"));
    }

    @Override
    public Order save(Order order) {
        return repository.save(order);
    }

    @Override
    public List<OrderResponse> findOrders(Long id) {
        return queryDslRepository.findOrders(id);
    }

    @Override
    public Map<Long, List<OrderItemResponse>> findOrderItems(List<Long> orderIds) {
        Map<Long, List<OrderItemResponse>> orderItems = queryDslRepository.findOrderItems(orderIds);
        return orderItems;
    }

    @Override
    public OrderDetailResponse findOrderDetail(long orderId) {
        return queryDslRepository.findOrderDetail(orderId);
    }

    @Override
    public boolean existsByMemberIdAndItemId(Long itemId, Long memberId) {
        return queryDslRepository.existsByMemberIdAndItemId(itemId, memberId);
    }
}
