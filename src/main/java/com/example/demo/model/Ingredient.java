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
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String pizza_size;
    @Column
    private Double price;
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.ALL
            }, mappedBy = "ingredients")
    @JsonIgnore
    private Set<Pizza> pizzas;
}
