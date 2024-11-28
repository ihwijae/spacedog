package com.spacedog.stock.domain;

import com.spacedog.member.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "stock")
@Getter
public class Stock extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  private Long itemId;

  private Long optionId;

  private int quantity;


  @Builder
  public Stock(Long id, Long itemId, Long optionId, int quantity) {
        this.id = id;
        this.itemId = itemId;
        this.optionId = optionId;
        this.quantity = quantity;
    }

    public Stock() {
    }
}
