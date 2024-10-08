package com.spacedog.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@AllArgsConstructor
@Table(name = "option_group_specs")
@Slf4j
@Builder
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

//    @OneToMany(mappedBy = "optionGroupSpecId")
//    private List<ItemOptionSpecification> optionSpecs = new ArrayList<>();




//    public void addItem (Item item) {
//
//        //기존 관계 제거
//        if(this.item != null) {
//            this.item.getItemOption().remove(this);
//        }
//
//        this.item = item;
//        item.getItemOption().add(this);
//    }







}