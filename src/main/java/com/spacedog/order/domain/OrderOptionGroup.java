//package com.spacedog.order.domain;
//
//import jakarta.persistence.*;
//
//@Entity
//public class OrderOptionGroup {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//
//    @Column(name = "order_option_group_name")
//    private String name;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_item_id")
//    private OrderItems orderItems;
//
//}
