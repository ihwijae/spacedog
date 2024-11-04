package com.spacedog.file.repository;

import com.spacedog.file.domain.FileData;

import java.util.List;
import java.util.Optional;

public interface FileRepository {


    public FileData save (FileData fileData);
    public Optional<FileData> findById(Long id);
    public List<FileData> findByItemId(Long itemId);
}
