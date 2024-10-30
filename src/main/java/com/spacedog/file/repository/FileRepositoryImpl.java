package com.spacedog.file.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.file.domain.FileData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository {

    private final FileJpaRepository fileJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public FileData save(FileData fileData) {
        return fileJpaRepository.save(fileData);
    }

    @Override
    public Optional<FileData> findById(Long id) {
        return fileJpaRepository.findById(id);
    }
}
