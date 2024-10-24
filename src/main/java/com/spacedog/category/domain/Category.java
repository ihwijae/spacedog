package com.spacedog.category.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Category {


    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "category_name")
    private String name;

    @Column(name = "depth")
    private Long depth;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;


    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    @OneToMany(mappedBy = "category")
    private List<CategoryItem> categoryItems;


    public Category() {
    }

    @Builder
    public Category(Long id, String name, Long depth, Category parent, List<Category> children, List<CategoryItem> categoryItems) {
        this.id = id;
        this.name = name;
        this.depth = depth;
        this.parent = parent;
        this.children = children;
        this.categoryItems = categoryItems;
    }
}
