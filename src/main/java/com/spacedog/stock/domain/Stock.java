package com.spacedog.stock.domain;

import com.spacedog.member.domain.BaseTimeEntity;
import com.spacedog.order.service.OrderException;
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


    public void addStock(int quantity) {
        this.quantity += quantity;
    }

    public void checkQuantity(int quantity) {

        if (this.quantity < quantity) {
            throw new OrderException("재고가 부족합니다. 남은 재고 : " + this.quantity );
        }
    }

    public void removeStock(int quantity) {
        int restStock = this.quantity - quantity;

        if( restStock < 0 ) {
            throw new OrderException("재고가 없습니다. 현재 재고: " + this.quantity );
        }

        this.quantity = restStock;
    }


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
