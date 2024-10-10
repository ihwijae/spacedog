package com.spacedog.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spacedog.item.dto.EditOptionGroupRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.utils.LogNode;
import org.hibernate.engine.internal.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "option_group_specs")
@Slf4j
public class ItemOptionGroupSpecification {


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



    public ItemOptionGroupSpecification() {
    }

    @Builder
    public ItemOptionGroupSpecification(Long id, String name, boolean exclusive, boolean basic, Item item) {
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