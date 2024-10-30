package com.spacedog.file.repository;

import com.spacedog.file.domain.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<FileData, Long> {
}
