package com.example.demo.service.impl;

import com.example.demo.model.Ingredient;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.service.abs.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ir;

    @Override
    public List<Ingredient> getAllIngredients() {
        return ir.findAll();
    }

    @Override
    public Ingredient getIngredientById(int id) {
        return ir.getReferenceById(id);
    }

    @Override
    public boolean saveOrUpdateIngredient(Ingredient ingredient) {
        try {
            ir.save(ingredient);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createIngredient(Ingredient ingredient) {
        try {
            ir.save(ingredient);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean deleteIngredient(int id) {
        try {
            ir.deleteById(id);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Ingredient> findIngredientsByPizzasId(Integer pizzas_id) {
        return ir.findIngredientsByPizzasId(pizzas_id);
    }
}
