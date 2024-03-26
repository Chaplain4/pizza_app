package com.example.demo.service.abs;


import com.example.demo.model.Ingredient;

import java.util.List;

public interface IngredientService {
    List<Ingredient> getAllIngredients();
    Ingredient getIngredientById(int id);
    boolean saveOrUpdateIngredient(Ingredient ingredient);
    boolean createIngredient(Ingredient ingredient);
}
