package com.example.demo.service.impl;

import com.example.demo.model.Pizza;
import com.example.demo.repository.PizzaRepository;
import com.example.demo.service.abs.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PizzaServiceImpl implements PizzaService {
    @Autowired
    private PizzaRepository pr;

    @Override
    public List<Pizza> getAllPizzas() {
        return pr.findAll();
    }

    @Override
    public Pizza getPizzaById(int id) {
        return pr.getReferenceById(id);
    }

    @Override
    public boolean saveOrUpdatePizza(Pizza pizza) {
        try {
            pr.save(pizza);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createPizza(Pizza pizza) {
        try {
            pr.save(pizza);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}
