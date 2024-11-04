package com.spacedog.file.repository;

import com.spacedog.file.domain.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileJpaRepository extends JpaRepository<FileData, Long> {

    List<FileData> findByItemId(Long id);
}
