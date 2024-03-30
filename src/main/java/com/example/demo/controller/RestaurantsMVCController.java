package com.example.demo.controller;


import com.example.demo.model.Address;
import com.example.demo.model.Restaurant;
import com.example.demo.service.abs.AddressService;
import com.example.demo.service.abs.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/restaurants")
public class RestaurantsMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private RestaurantService rs;

    @Autowired
    private AddressService as;

    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("restaurants", rs.getAllRestaurants());
        model.addAttribute("address", new Address());
        model.addAttribute("restaurant", new Restaurant());
        logger.info("restaurants added");
        return "restaurant_form";
    }

    @PostMapping("/create_restaurant")
    public String submitForm(@ModelAttribute("restaurant") Restaurant restaurant) {
        System.out.println("begin posting");
        try {
            System.out.println(restaurant);
            as.createAddress(restaurant.getAddress());
            rs.createRestaurant(restaurant);
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
            rs.deleteRestaurant(id);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
        logger.info("showForm started");
        model.addAttribute("restaurant", rs.getRestaurantById(id));
        logger.info("restaurant added");
        return "restaurant_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("restaurant") Restaurant restaurant) {
        System.out.println("begin editing");
        try {
            System.out.println(restaurant);
            as.saveOrUpdateAddress(restaurant.getAddress());
            rs.saveOrUpdateRestaurant(restaurant);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
