package com.spacedog.file.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.file.domain.ItemImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository {

    private final FileJpaRepository fileJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public ItemImage save(ItemImage itemImage) {
        return fileJpaRepository.save(itemImage);
    }

    @Override
    public Optional<ItemImage> findById(Long id) {
        return fileJpaRepository.findById(id);
    }

    @Override
    public List<ItemImage> findByItemId(Long itemId) {
        return fileJpaRepository.findByItemId(itemId);
    }
}
