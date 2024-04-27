package com.example.demo.controller;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.model.Address;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.abs.AddressService;
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
@RequestMapping("/addresses")
public class AddressesMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);
    @Autowired
    private AddressService as;

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

        logger.info("showForm started");
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("address", new Address());
        logger.info("addresses added");
        return "address_form";
    }

    @PostMapping("/create_address")
    public String submitForm(@ModelAttribute("address") Address address) {
        System.out.println("begin posting");
        try {
            System.out.println(address);
            as.createAddress(address);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
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
            as.deleteAddress(id);
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
        model.addAttribute("address", as.getAddressById(id));
        logger.info("address added");
        return "address_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("address") Address address) {
        System.out.println("begin editing");
        try {
            System.out.println(address);
            as.saveOrUpdateAddress(address);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
