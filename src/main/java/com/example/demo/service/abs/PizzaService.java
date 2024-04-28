package com.example.demo.service.abs;


import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.Pizza;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface PizzaService {
    List<PizzaDTO> getAllPizzas();
    Pizza getPizzaById(int id);
    PizzaDTO saveOrUpdatePizza(Integer pizzaID, PizzaDTO pizzaDTO);
    PizzaDTO createPizza(PizzaDTO pizzaDTO);
    String deletePizza(Integer pizzaID);
    List<Pizza> findPizzasByIngredientsId(Integer ingredients_id);
    List<Pizza> findPizzasByOrdersId(Integer orders_id);

    void mapDtoToEntity(PizzaDTO pizzaDTO, Pizza pizza);

    PizzaDTO mapEntityToDto(Pizza pizza);
    void addIngredientToPizza(Integer pizzaId, Integer ingredientId);
    void addIngredientsToPizza(Integer pizzaId, Set<Integer> ingredientId);
    void clearIngredients(Integer pizzaId);
}
