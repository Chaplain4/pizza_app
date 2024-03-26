package com.example.demo.controller;


import com.example.demo.model.Address;
import com.example.demo.model.Restaurant;
import com.example.demo.service.abs.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurants")
public class RestaurantsMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private RestaurantService rs;

    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("restaurants", rs.getAllRestaurants());
        logger.info("restaurants added");
        return "restaurant_form";
    }

    @PostMapping("/list")
    public String submitForm(@ModelAttribute("Restaurant") Restaurant restaurant, @ModelAttribute("Address") Address address) {
        try {
            restaurant.setAddress(address);
            System.out.println(restaurant);
            rs.createRestaurant(restaurant);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
