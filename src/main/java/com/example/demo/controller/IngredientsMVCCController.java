package com.example.demo.controller;

import com.example.demo.model.Ingredient;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.abs.IngredientService;
import com.example.demo.service.abs.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/ingredients")
public class IngredientsMVCCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private IngredientService is;

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
        }
        else return new HashSet<>(acs.findRolesByUserId(user.getId()));
    }


    @GetMapping("/list")
    public String showForm(Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        logger.info("showForm started");
        model.addAttribute("ingredients", is.getAllIngredients());
        model.addAttribute("ingredient", new Ingredient());
        logger.info("ingredients added");
        return "ingredient_form";
    }

    @PostMapping("/create_ingredient")
    public String submitForm(@ModelAttribute("ingredient") Ingredient ingredient) {
        System.out.println("begin posting");
        try {
            System.out.println(ingredient);
            is.createIngredient(ingredient);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/delete/{id}")
    public String remove(@PathVariable(name = "id") Integer id, Model model) {
        System.out.println("begin posting");
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        try {
            is.deleteIngredient(id);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        logger.info("showForm started");
        model.addAttribute("ingredient", is.getIngredientById(id));
        logger.info("restaurant added");
        return "ingredient_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("ingredient") Ingredient ingredient) {
        System.out.println("begin editing");
        try {
            System.out.println(ingredient);
            is.saveOrUpdateIngredient(ingredient);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
