package com.example.demo.repository;

import com.example.demo.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
    List<Pizza> findPizzasByIngredientsId(Integer ingredients_id);
    List<Pizza> findPizzasByOrdersId(Integer orders_id);
}