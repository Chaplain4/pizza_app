package com.example.demo.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String pwd;
    @Column
    private String phone;
    @Column(columnDefinition = "boolean default false")
    private Boolean isActivated;
    @Column
    private Integer bonus_coins;
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "accounts_roles", joinColumns = { @JoinColumn(name = "account_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> roles;
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "accounts_addresses", joinColumns = { @JoinColumn(name = "account_id") }, inverseJoinColumns = {
            @JoinColumn(name = "address_id") })
    private Set<Address> addresses;
    @ManyToOne()
    @JoinColumn(name = "restaurant_id")
    @Nullable
    private Restaurant restaurant;
    @OneToMany(mappedBy = "account")
    @Nullable
    private Set<Order> orders;
    @OneToMany(mappedBy = "account")
    @Nullable
    private Set<Feedback> feedbacks;
}
