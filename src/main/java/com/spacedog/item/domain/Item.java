package com.spacedog.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spacedog.category.domain.Category;
import com.spacedog.category.domain.CategoryItem;
import com.spacedog.generic.Money;
import com.spacedog.generic.MoneyConverter;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.dto.ItemEditRequest;
import com.spacedog.item.dto.OptionGroupRequest;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.item.service.ItemMapper;
import com.spacedog.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Slf4j
public class Item {



    @Id @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "item_price")
    @Convert(converter = MoneyConverter.class)
    private Money price;

    @Column(name = "stock_quantity")
    private int stockQuantity;


    @OneToMany(mappedBy = "item")
    private List<CategoryItem> category = new ArrayList<>();

    @Builder
    public Item(Long id, String name, String description, Long memberId, Money price, int stockQuantity, CategoryItem category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.memberId = memberId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category.add(category);
    }

    public Item() {
    }


    public void addCategory(CategoryItem category) {
        this.category.add(category);
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            throw new NotEnoughStockException("재고가 없습니다");
        }

        this.stockQuantity = restStock;

    }



   public void addMember(Member member) {
        this.memberId = member.getId();
   }


   public void itemUpdate(ItemEditRequest request) {


       // 연관관계 설정
       this.name = request.getName();
        this.description = request.getDescription();
        this.price = request.getPrice();
        this.stockQuantity = request.getStockQuantity();


   }




}
