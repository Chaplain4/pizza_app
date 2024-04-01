package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.Address;
import com.example.demo.service.abs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersMVCController {

    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private UserService acs;
    @Autowired
    private RestaurantService rs;

    @Autowired
    private RoleService ros;

    @Autowired
    private AddressService as;

    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("user", new User());
        model.addAttribute("restaurants", rs.getAllRestaurants());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("address", new Address());
        model.addAttribute("roles", ros.getAllRoles());
        logger.info("restaurants added");
        return "user_form";
    }

    @PostMapping("/create_user")
    public String submitForm(@ModelAttribute("user") User user) {
        System.out.println("begin posting");
        try {
            System.out.println(user);
            user.getAddresses().forEach(address -> {
                as.createAddress(address);
            });
            acs.createUser(user);
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
            acs.deleteUser(id);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
        logger.info("showForm started");
        model.addAttribute("user", acs.getUserById(id));
        logger.info("user added");
        return "user_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("user") User user) {
        System.out.println("begin editing");
        try {
            System.out.println(user);
            user.getAddresses().forEach(address -> {
                as.saveOrUpdateAddress(address);
            });
            acs.saveOrUpdateUser(user);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
