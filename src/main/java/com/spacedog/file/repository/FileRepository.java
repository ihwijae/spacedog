package com.spacedog.file.repository;

import com.spacedog.file.domain.FileData;

import java.util.Optional;

public interface FileRepository {


    public FileData save (FileData fileData);
    public Optional<FileData> findById(Long id);
}
