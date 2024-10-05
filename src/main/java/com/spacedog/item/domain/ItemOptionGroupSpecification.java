package com.spacedog.item.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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


    @Builder
    public ItemOptionGroupSpecification(Long id, String name, boolean exclusive, boolean basic, List<ItemOptionSpecification> optionSpecs) {
        this.id = id;
        this.name = name;
        this.exclusive = exclusive;
        this.basic = basic;
        this.optionSpecs = optionSpecs;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "exclusive")
    private boolean exclusive;

    @Column(name = "basic")
    private boolean basic;

    @OneToMany
    @JoinColumn(name = "option_group_spec_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<ItemOptionSpecification> optionSpecs = new ArrayList<>();



    public void addOptionSpecs(ItemOptionSpecification spec) {
        log.info("spec = {}", spec);
        log.info("optionSpecs = {}", this.optionSpecs);
        this.optionSpecs.add(spec);
    }


}