package com.spacedog.category.domain;

import com.spacedog.item.domain.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "item_category")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CategoryItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;



    public void addRelation(Item item, Category category) {
        log.info("item 객체= {}", item.getId());
        log.info("카테고리 객체= {}", category.getId());

        CategoryItem categoryItem = CategoryItem.builder()
                .item(item)
                .category(category)
                .build();

        item.getCategory().add(categoryItem);
        category.getCategoryItems().add(categoryItem);
    }
}
