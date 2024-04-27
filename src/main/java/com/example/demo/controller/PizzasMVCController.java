package com.example.demo.controller;


import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Pizza;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.abs.IngredientService;
import com.example.demo.service.abs.PizzaService;
import com.example.demo.service.abs.UserService;
import com.example.demo.service.impl.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/pizzas")
public class PizzasMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private CustomUserDetailsService cuds;

    @Autowired
    private IngredientService is;

    @Autowired
    private PizzaService ps;

    @Autowired
    private UserService acs;

    @ModelAttribute("currentRoles")
    public Set<Role> currentRoles() {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            return new HashSet<>();
        } else return new HashSet<>(acs.findRolesByUserId(user.getId()));
    }

    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        List<PizzaDTO> pizzas = ps.getAllPizzas();
        model.addAttribute("pizzas", ps.getAllPizzas());
        model.addAttribute("allIngredients", is.getAllIngredients());
        model.addAttribute("pizza", new Pizza());
        logger.info("pizzas added");
        logger.info(ps.getAllPizzas().toString());
        return "pizza_form";
    }

    @PostMapping("/create_pizza")
    public String submitForm(@ModelAttribute("pizza") PizzaDTO pizza) {
        System.out.println("begin posting");
        List<Ingredient> all = is.getAllIngredients();
        Set<Ingredient> ingredients = pizza.getIngredients();
        Set<Ingredient> newIngredients = new HashSet<>();
        ingredients.forEach(ingredient -> {
            if (ingredient.getPizza_size().equals(pizza.getPizza_size())) {
                newIngredients.add(ingredient);
            } else {
                all.forEach(ingredient1 -> {
                    if (ingredient1.getName().equals(ingredient.getName()) && ingredient1.getPizza_size().equals(pizza.getPizza_size())) {
                        newIngredients.add(ingredient1);
                    }
                });
            }
        });
        pizza.setIngredients(newIngredients);
        ps.createPizza(pizza);
        return "request_success";
    }

    @GetMapping("/delete/{id}")
    public String remove(@PathVariable(name = "id") Integer id, Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
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
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        List <PizzaDTO> all = ps.getAllPizzas();
        PizzaDTO pizzaDTO = new PizzaDTO();
        for (PizzaDTO p : all) {
            if (p.getId().equals(id)) {
                pizzaDTO = p;
            }
        }
        model.addAttribute("pizza", pizzaDTO);
        model.addAttribute("allIngredients", is.getAllIngredients());
        logger.info("pizza added");
        return "pizza_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("pizza") PizzaDTO pizza) {
        Pizza oldPizza = ps.getPizzaById(pizza.getId());
        ps.deletePizza(pizza.getId());
        Set<Ingredient> newIngredients = new HashSet<>();
        List<Ingredient> all = is.getAllIngredients();
        Set<Ingredient> ingredients = oldPizza.getIngredients();
        pizza.getIngredients().forEach(ingredient -> {
            if (ingredient.getPizza_size().equals(pizza.getPizza_size())) {
                newIngredients.add(ingredient);
            } else {
                all.forEach(ingredient1 -> {
                    if (ingredient1.getName().equals(ingredient.getName()) && ingredient1.getPizza_size().equals(pizza.getPizza_size())) {
                        newIngredients.add(ingredient1);
                    }
                });
            }
        });
        pizza.setIngredients(newIngredients);
        pizza.setId(null);
        ps.createPizza(pizza);
        return "request_success";
    }
}

