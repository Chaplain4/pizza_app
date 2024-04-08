package com.example.demo.dto;

import com.example.demo.model.Pizza;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    private Integer id;
    private String name;
    private String pizza_size;
    private Double price;
    private Set<Pizza> pizzas;
}
