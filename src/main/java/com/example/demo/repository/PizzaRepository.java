package com.example.demo.repository;

import com.example.demo.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
    List<Pizza> findPizzasByIngredientsId(Integer ingredients_id);
    List<Pizza> findPizzasByOrdersId(Integer orders_id);
    @Modifying
    @Query(value = "INSERT INTO pizzas_ingredients (pizza_id, ingredient_id) VALUES (:pizzaId, :ingredientId)", nativeQuery = true)
    void addIngredient(@Param("pizzaId") Integer pizzaId, @Param("ingredientId") Integer ingredientId);
    @Modifying
    @Query(value = "INSERT INTO pizzas_ingredients (pizza_id, ingredient_id) VALUES (:pizzaId, :ingredientIds)", nativeQuery = true)
    void addIngredients(@Param("pizzaId") List<Integer> pizzaId, @Param("ingredientIds") Set<Integer> ingredientIds);

    @Modifying
    @Query(value = "DELETE FROM pizzas_ingredients WHERE  (pizza_id = :pizzaId)", nativeQuery = true)
    void clearIngredients(@Param("pizzaId") Integer pizzaId);
}