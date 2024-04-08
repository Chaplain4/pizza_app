package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
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
    private String pizza_size;
    @Column
    private Double price;
    @Column
    private Boolean menu_item;
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "pizzas_ingredients", joinColumns = {@JoinColumn(name = "pizza_id")}, inverseJoinColumns = {
            @JoinColumn(name = "ingredient_id")})
    private Set<Ingredient> ingredients;
    @ManyToMany(mappedBy = "pizzas")
    @JsonIgnore
    private Set<Order> orders;

    public void addIngredient(Ingredient i) {
        i.getPizzas().add(this);
        this.ingredients.add(i);
    }

    public void removeIngredient(Ingredient i) {
        this.getIngredients().remove(i);
        i.getPizzas().remove(this);
    }

    public void removeIngredients() {
        for (Ingredient i : new HashSet<>(ingredients)) {
            removeIngredient(i);
        }
    }

    public void addOrder(Order o) {
        this.orders.add(o);
        o.getPizzas().add(this);
    }

    public void removeOrder(Order o) {
        this.getOrders().remove(o);
        o.getPizzas().remove(this);
    }

    public void removeOrders() {
        for (Order o : new HashSet<>(orders)) {
            removeOrder(o);
        }
    }
}
