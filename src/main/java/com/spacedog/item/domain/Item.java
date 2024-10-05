package com.spacedog.item.domain;

import com.spacedog.category.domain.Category;
import com.spacedog.generic.Money;
import com.spacedog.generic.MoneyConverter;
import com.spacedog.item.dto.CreateItemRequest;
import com.spacedog.item.exception.NotEnoughStockException;
import com.spacedog.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
//    @Convert(converter = MoneyConverter.class)
    private Money price;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "category_id")
    private Long categoryId;


    @OneToMany
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<ItemOptionGroupSpecification> itemOption = new ArrayList<>();


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

    public static Item createItem(CreateItemRequest request, Member member, Category category) {
        return Item.builder()
                .name(request.getName())
                .memberId(member.getId())
                .categoryId(category.getId())
                .price(request.getPrice())
                .description(request.getDescription())
                .stockQuantity(request.getStockQuantity())
                .build();
    }
}
