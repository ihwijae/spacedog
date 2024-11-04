package com.spacedog.option.domain;

import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.EditOptionGroupRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Table(name = "option_group_specs")
@Slf4j
public class OptionGroupSpecification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_group_spec_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "exclusive")
    private boolean exclusive;
    @Column(name = "basic")
    private boolean basic;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;



    public OptionGroupSpecification() {
    }

    @Builder
    public OptionGroupSpecification(Long id, String name, boolean exclusive, boolean basic, Item item) {
        this.id = id;
        this.name = name;
        this.exclusive = exclusive;
        this.basic = basic;
        this.item = item;
    }


    public void update(EditOptionGroupRequest request) {
        this.name = request.getName();
        this.exclusive = request.isExclusive();
        this.basic = request.isBasic();
    }




}