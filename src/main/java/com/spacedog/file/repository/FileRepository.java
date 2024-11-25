package com.spacedog.file.repository;

import com.spacedog.file.domain.ItemImage;

import java.util.List;
import java.util.Optional;

public interface FileRepository {


    public ItemImage save (ItemImage itemImage);
    public Optional<ItemImage> findById(Long id);
    public List<ItemImage> findByItemId(Long itemId);
}
