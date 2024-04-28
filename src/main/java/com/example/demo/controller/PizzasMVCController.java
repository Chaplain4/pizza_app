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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        List<PizzaDTO> pizzas = new ArrayList<>();
        for (PizzaDTO p : ps.getAllPizzas()) {
            if (p.getMenu_item()!=null && p.getMenu_item()) {
                pizzas.add(p);
            }
        }
        model.addAttribute("pizzas", pizzas);
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

    @GetMapping("/rivals")
    public String showRivals(Model model) {
        String html = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://www.dominos.by/ru/minsk/product/mgrc/"); // Ваш URL

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                html = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String dominosMargharita = html.substring(html.indexOf("\\u0022price\\u0022: ")+19);
        dominosMargharita = dominosMargharita.replaceAll(",.+","");
        dominosMargharita = dominosMargharita.substring(0,dominosMargharita.indexOf('\n'));
        model.addAttribute("dominosM", dominosMargharita);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://www.dominos.by/ru/minsk/product/tp/"); // Ваш URL

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                html = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String dominosPepperoni = html.substring(html.indexOf("\\u0022price\\u0022: ")+19);
        dominosPepperoni = dominosPepperoni.replaceAll(",.+","");
        dominosPepperoni = dominosPepperoni.substring(0,dominosPepperoni.indexOf('\n'));
        model.addAttribute("dominosP", dominosPepperoni);
        return "rivals";
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

