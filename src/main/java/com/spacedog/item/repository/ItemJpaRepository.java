package com.spacedog.item.repository;

import com.spacedog.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByName(String name);

}
