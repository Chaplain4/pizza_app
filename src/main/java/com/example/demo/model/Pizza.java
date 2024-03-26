package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pizzas")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private Double price;
    @Column
    private Boolean menu_item;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "pizzas_ingredients", joinColumns = {@JoinColumn(name = "pizza_id")}, inverseJoinColumns = {
            @JoinColumn(name = "ingredient_id")})
    private Set<Ingredient> ingredients;
    @ManyToMany(mappedBy = "pizzas")
    @JsonIgnore
    private Set<Order> orders;
}
