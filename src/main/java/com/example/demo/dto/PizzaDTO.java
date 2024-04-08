package com.example.demo.dto;

import com.example.demo.model.Ingredient;
import com.example.demo.model.Order;
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
public class PizzaDTO {
    private Integer id;
    private String name;
    private String pizza_size;
    private Double price;
    private Boolean menu_item;
    private Set<Ingredient> ingredients  = new HashSet<>();
    private Set<Order> orders  = new HashSet<>();
}
