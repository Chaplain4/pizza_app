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
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String open_hours;
    @OneToOne()
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "restaurant")
    @Nullable
    private Set<User> users;
    @OneToMany(mappedBy = "restaurant")
    @Nullable
    private Set<Order> orders;
}
