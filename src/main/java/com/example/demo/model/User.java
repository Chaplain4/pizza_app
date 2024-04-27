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
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @Column
    private String phone;
    @Column(columnDefinition = "boolean default false")
    private Boolean isActivated;
    @Column
    private Integer bonus_coins;
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "users_roles", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id")})
    private Set<Role> roles;
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "users_addresses", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "address_id")})
    private Set<Address> addresses;
    @ManyToOne()
    @JoinColumn(name = "restaurant_id")
    @Nullable
    private Restaurant restaurant;
    @OneToMany(mappedBy = "user")
    @Nullable
    private Set<Order> orders;
    @OneToMany(mappedBy = "user")
    @Nullable
    private Set<Feedback> feedbacks;
}
