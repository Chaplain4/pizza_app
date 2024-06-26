package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String street;
    @Column
    private String building;
    @Column
    private Integer entrance;
    @Column
    private Integer floor;
    @Column
    private Integer apartment;
    @Column
    private Boolean has_intercom;
    @Column
    private String door_code;
    @Column
    private String comment;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = { CascadeType.MERGE, CascadeType.PERSIST }, mappedBy = "addresses")
    @JsonIgnore
    private Set<User> users;
    @OneToMany(mappedBy = "address")
    @Nullable
    private Set<Order> orders;
}
