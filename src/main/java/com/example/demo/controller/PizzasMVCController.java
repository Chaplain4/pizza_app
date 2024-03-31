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
//        Set<Pizza> p = new HashSet<>(ps.getAllPizzas());
//        List<Ingredient> i = new ArrayList<>(is.getAllIngredients());
//        i.forEach(ingredient -> {
//            Set<Integer> ids = new HashSet<>();
//            p.forEach(pizza -> {
//                ingredient.getPizzas().forEach(pizza1 -> {
//                    if (pizza1.getId().equals(pizza.getId())) {
//                        Set<Ingredient> i1 =pizza.getIngredients();
//                        i1.add(ingredient);
//                        pizza.setIngredients(i1);
//                    }
//                });
//            });
//        });

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
            List<Ingredient> pizzaIngredients = new ArrayList<>(pizza.getIngredients());
            pizzaIngredients.forEach(ingredient -> {
                if (!Objects.equals(ingredient.getPizza_size(), pizza.getPizza_size())) {
                    all.forEach(ingredient1 -> {
                        if (ingredient1.getPizza_size().equals(pizza.getPizza_size()) && ingredient1.getName().equals(ingredient.getName())) {
                            pizzaIngredients.remove(ingredient);
                            pizzaIngredients.add(ingredient1);
                        }
                    });
                    pizza.setIngredients(new HashSet<>(pizzaIngredients));
                }
            });
            System.out.println(pizza);
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
        model.addAttribute("restaurant", ps.getPizzaById(id));
        logger.info("pizza added");
        return "pizza_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("pizza") Pizza pizza) {
        System.out.println("begin editing");
        try {
            System.out.println(pizza);
            ps.saveOrUpdatePizza(pizza);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
