package com.example.demo.controller;


import com.example.demo.model.Ingredient;
import com.example.demo.model.Pizza;
import com.example.demo.service.abs.IngredientService;
import com.example.demo.service.abs.PizzaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/pizzas")
public class PizzasMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private IngredientService is;

    @Autowired
    private PizzaService ps;

    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("pizzas", ps.getAllPizzas());
        model.addAttribute("allIngredients", is.getAllIngredients());
        model.addAttribute("pizza", new Pizza());
        logger.info("pizzas added");
        return "pizza_form";
    }

    @PostMapping("/create_pizza")
    public String submitForm(@ModelAttribute("pizza") Pizza pizza) {
        System.out.println("begin posting");
        System.out.println(pizza);
        try {
            List<Ingredient> all = is.getAllIngredients();
            Set<Ingredient> pizzaIngredients = new HashSet<>();
            Set<Ingredient> counter = new HashSet<>(pizza.getIngredients());
            List<String> names = new ArrayList<>();
            counter.forEach(ingredient -> {
                names.add(ingredient.getName());
            });
            all.forEach(ingredient -> {
                if (Objects.equals(ingredient.getPizza_size(), pizza.getPizza_size()) && names.contains(ingredient.getName())) {
                    pizzaIngredients.add(ingredient);
                }
            });
            pizza.setIngredients(pizzaIngredients);
            ps.createPizza(pizza);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/delete/{id}")
    public String remove(@PathVariable(name = "id") Integer id) {
        System.out.println("begin posting");
        try {
            ps.deletePizza(id);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
        logger.info("showForm started");
        model.addAttribute("pizza", ps.getPizzaById(id));
        model.addAttribute("allIngredients", is.getAllIngredients());
        logger.info("pizza added");
        return "pizza_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("pizza") Pizza pizza) {
        System.out.println("begin editing");
        try {
            List<Ingredient> all = is.getAllIngredients();
            List<Ingredient> pizzaIngredients = new ArrayList<>();
            Set<Ingredient> counter = new HashSet<>(pizza.getIngredients());
            List<String> names = new ArrayList<>();
            counter.forEach(ingredient -> {
                names.add(ingredient.getName());
            });
            all.forEach(ingredient -> {
                if (Objects.equals(ingredient.getPizza_size(), pizza.getPizza_size()) && names.contains(ingredient.getName())) {
                    pizzaIngredients.add(ingredient);
                }
            });
            pizzaIngredients.forEach(ingredient1 -> {
                ingredient1.setPizzas(null);
            });
            pizza.setIngredients(new HashSet<>(pizzaIngredients));
            ps.saveOrUpdatePizza(pizza);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
