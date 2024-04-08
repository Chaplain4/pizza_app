package com.example.demo.repository;

import com.example.demo.model.Ingredient;
import com.example.demo.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findIngredientsByPizzasId(Integer pizzas_id);
    public Ingredient findByName(String ingredientName);
}
