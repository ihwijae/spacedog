//package com.spacedog.mock;
//
//import com.spacedog.order.domain.Order;
//import com.spacedog.order.domain.OrderStatus;
//import com.spacedog.order.repository.OrderRepository;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.security.core.parameters.P;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.concurrent.atomic.AtomicLong;
//import java.util.function.Function;
//
//public class FakeOrderRepository implements OrderRepository {
//
//    private final AtomicLong autoGeneratedId = new AtomicLong(0);
//    private final List<Order> data = new ArrayList<>();
//
//
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends Order> S saveAndFlush(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Order> List<S> saveAllAndFlush(Iterable<S> entities) {
//        return List.of();
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<Order> entities) {
//
//    }
//
//    @Override
//    public void deleteAllByIdInBatch(Iterable<Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//
//    }
//
//    @Override
//    public Order getOne(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Order getById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Order getReferenceById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public <S extends Order> List<S> findAll(Example<S> example) {
//        return List.of();
//    }
//
//    @Override
//    public <S extends Order> List<S> findAll(Example<S> example, Sort sort) {
//        return List.of();
//    }
//
//    @Override
//    public void deleteInBatch(Iterable<Order> entities) {
//        OrderRepository.super.deleteInBatch(entities);
//    }
//
//    @Override
//    public <S extends Order> List<S> saveAll(Iterable<S> entities) {
//        return List.of();
//    }
//
//    @Override
//    public List<Order> findAll() {
//        return List.of();
//    }
//
//    @Override
//    public List<Order> findAllById(Iterable<Long> longs) {
//        return List.of();
//    }
//
//    @Override
//    public <S extends Order> S save(S entity) {
//        if(entity.getId() == null || entity.getId() == 0) {
//            Order order = Order.builder()
//                    .id(autoGeneratedId.incrementAndGet())
//                    .name(entity.getName())
//                    .phone(entity.getPhone())
//                    .orderDate(entity.getOrderDate())
//                    .orderStatus(OrderStatus.ORDER)
//                    .deliveryId(entity.getDeliveryId())
//                    .build();
//
//            data.add(order);
//            return (S) order;
//        } else {
//            data.removeIf(i -> Objects.equals(i.getId(), entity.getId()));
//            data.add(entity);
//            return (S) entity;
//        }
//    }
//
//    @Override
//    public Optional<Order> findById(Long aLong) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return false;
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//
//    }
//
//    @Override
//    public void delete(Order entity) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends Order> entities) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public List<Order> findAll(Sort sort) {
//        return List.of();
//    }
//
//    @Override
//    public Page<Order> findAll(Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends Order> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends Order> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends Order> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends Order> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public <S extends Order, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//        return null;
//    }
//}
