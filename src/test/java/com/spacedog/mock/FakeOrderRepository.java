package com.spacedog.mock;

import com.spacedog.order.domain.Order;
import com.spacedog.order.repository.OrderRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FakeOrderRepository implements OrderRepository {


    @Override
    public void flush() {

    }

    @Override
    public <S extends Order> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Order> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Order> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Order getOne(Long aLong) {
        return null;
    }

    @Override
    public Order getById(Long aLong) {
        return null;
    }

    @Override
    public Order getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Order> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Order> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public void deleteInBatch(Iterable<Order> entities) {
        OrderRepository.super.deleteInBatch(entities);
    }

    @Override
    public <S extends Order> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public List<Order> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public <S extends Order> S save(S entity) {
        return null;
    }

    @Override
    public Optional<Order> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Order entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Order> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Order> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Order> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Order> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Order> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Order> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Order, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
