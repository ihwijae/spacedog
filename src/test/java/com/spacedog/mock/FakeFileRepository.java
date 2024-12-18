package com.spacedog.mock;

import com.spacedog.file.domain.ItemImage;
import com.spacedog.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class FakeFileRepository implements FileRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private List<ItemImage> data = new ArrayList<>();

    @Override
    public ItemImage save(ItemImage itemImage) {

        if(itemImage.getId() == null || itemImage.getId() == 0) {
            ItemImage newFile = ItemImage.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .storeFilename(itemImage.getStoreFileName())
                    .uploadFilename(itemImage.getUploadFileName())
                    .build();

            log.info("file_id = {}", newFile.getId());

            data.add(newFile);
            return newFile;
        } else {
            data.removeIf(f -> Objects.equals(f.getId(), itemImage.getId()));
            data.add(itemImage);
            return itemImage;
        }
    }

    @Override
    public Optional<ItemImage> findById(Long id) {
        return data.stream()
                .filter(f -> f.getId().equals(id))
                .findAny();
    }

    @Override
    public List<ItemImage> findByItemId(Long itemId) {
        return List.of();
    }
}
