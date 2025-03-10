//package com.spacedog.mock;
//
//import com.spacedog.item.domain.Item;
//import com.spacedog.item.dto.*;
//import com.spacedog.item.repository.ItemRepositoryPort;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class FakeItemRepository implements ItemRepositoryPort {
//
//    private final AtomicLong autoGeneratedId = new AtomicLong(0); // 0으로 초기화한 이유는 기본값을 0부터 시작해서 ID처럼 순차적으로 증가하는 고유한 값을 생성하기 위해서
//    private final List<Item> data = new ArrayList<>();
//
//
//    @Override
//    public Optional<Item> findById(Long id) {
//        return data.stream()
//                .filter(i -> i.getId().equals(id))
//                .findAny();
//    }
//
//    @Override
//    public Item save(Item item) {
//        if (item.getId() == null || item.getId() == 0) {
//            Item newItem = Item.builder()
//                    .id(autoGeneratedId.incrementAndGet())
//                    .name(item.getName())
//                    .description(item.getDescription())
//                    .price(item.getPrice())
//                    .memberId(item.getMemberId())
//                    .build();
//
//            data.add(newItem);
//            return newItem;
//        } else {
//            data.removeIf(i -> Objects.equals(i.getId(), item.getId())); // 해당 조건이 true인 요소를 리스트에서 제거 (현재 리스트 사용자id와 새로운 사용자id가 동일한지 비교)
//            data.add(item);
//            return item;
//        }
//    }
//
//    @Override
//    public void delete(Long id) {
//        data.removeIf(i -> Objects.equals(i.getId(), id));
//    }
//
//    @Override
//    public void deleteAll(Iterable<Item> items) {
//
//    }
//
//    @Override
//    public void update(Long itemId, ItemEditRequest request) {
//
//    }
//
//    @Override
//    public Optional<Item> findByName(String name) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existByName(String name) {
//        return data.stream()
//                .anyMatch(i -> i.getName().equals(name));
//    }
//
//    @Override
//    public List<SearchItemResponse> getItems(SearchItemRequest request) {
//        return List.of();
//    }
//
//    @Override
//    public List<FindItemAllResponse> findItemsAll(int pageNo, int pageSize) {
//        return List.of();
//    }
//
//    @Override
//    public Optional<Item> findByItemWithCategory(Long id) {
//        return Optional.empty();
//    }
//
//    @Override
//    public List<ItemDetailResponse> itemDetail(Long itemId) {
//        return List.of();
//    }
//}
