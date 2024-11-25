package com.spacedog.file.repository;

import com.spacedog.file.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileJpaRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemId(Long id);

}
