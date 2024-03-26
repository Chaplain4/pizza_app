package com.example.demo.service.abs;


import com.example.demo.model.Pizza;

import java.util.List;

public interface PizzaService {
    List<Pizza> getAllPizzas();
    Pizza getPizzaById(int id);
    boolean saveOrUpdatePizza(Pizza pizza);
    boolean createPizza(Pizza pizza);
}
