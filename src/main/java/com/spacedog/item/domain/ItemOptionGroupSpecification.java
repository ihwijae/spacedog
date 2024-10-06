package com.spacedog.item.domain;

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
@NoArgsConstructor
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

    @Column(name = "item_id")
    private Long itemId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "option_group_spec_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<ItemOptionSpecification> optionSpecs = new ArrayList<>();


    @Builder
    public ItemOptionGroupSpecification(Long id, String name, boolean exclusive, boolean basic, List<ItemOptionSpecification> optionSpecs, Long itemId) {
        this.id = id;
        this.name = name;
        this.exclusive = exclusive;
        this.basic = basic;
        this.optionSpecs = optionSpecs;
        this.itemId = itemId;
    }




}