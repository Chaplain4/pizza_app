package com.example.demo.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne()
    @JoinColumn(name = "account_id")
    @Nullable
    private Account account;
    @ManyToOne()
    @JoinColumn(name = "restaurant_id")
    @Nullable
    private Restaurant restaurant;
    @Column(columnDefinition = "boolean default false")
    private Boolean to_deliver;
    @ManyToOne()
    @JoinColumn(name = "address_id")
    @Nullable
    private Address address;
    @Column
    private Timestamp created;
    @Column
    private Timestamp confirmed;
    @Column
    private Timestamp assembled;
    @Column
    private Timestamp delivered;
    @Column
    private String comment;
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "orders_pizzas", joinColumns = { @JoinColumn(name = "order_id") }, inverseJoinColumns = {
            @JoinColumn(name = "pizzza_id") })
    private Set<Pizza> pizzas;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "orders_side_items", joinColumns = { @JoinColumn(name = "order_id") }, inverseJoinColumns = {
            @JoinColumn(name = "side_item_id") })
    private Set<SideItem> side_items;
    @OneToOne()
    @JoinColumn(name = "feedback_id")
    @Nullable
    private Feedback feedback;
}
