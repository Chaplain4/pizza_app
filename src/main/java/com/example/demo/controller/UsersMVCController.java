package com.example.demo.controller;

import com.example.demo.model.Restaurant;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.Address;
import com.example.demo.service.abs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

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
        Set<User> all = new HashSet<>();
        for (User u : acs.getAllUsers()) {
            all.add(acs.getUserById(u.getId()));
        }
        model.addAttribute("user", new User());
        model.addAttribute("restaurants", rs.getAllRestaurants());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("address", new Address());
        model.addAttribute("roles", ros.getAllRoles());
        model.addAttribute("users", all);
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        logger.info("users added");
        return "account_form";
    }

    @Transactional
    @GetMapping("/clear/{userid}")
    public String clearRoles(Model model, @PathVariable(name = "userid") String userId) {
        logger.info("showForm started");
        Set<User> all = new HashSet<>();
        for (User u : acs.getAllUsers()) {
            all.add(acs.getUserById(u.getId()));
        }
        model.addAttribute("restaurants", rs.getAllRestaurants());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("address", new Address());
        model.addAttribute("roles", ros.getAllRoles());
        model.addAttribute("users", all);
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        User auser = acs.getUserById(Integer.parseInt(userId));
        auser.setRestaurant(null);
        ros.clearRoles(auser.getId());
        acs.saveOrUpdateUser(auser);
        model.addAttribute("user", new User());
        model.addAttribute("restaurants", rs.getAllRestaurants());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("address", new Address());
        model.addAttribute("roles", ros.getAllRoles());
        model.addAttribute("users", acs.getAllUsers());
        logger.info("users added");
        return "account_form";
    }

    @Transactional
    @GetMapping("/assignrole/{userid}")
    public String assignRole(Model model, @PathVariable(name = "userid") String userId, @ModelAttribute("roleId") String restaurantId) {
        logger.info("showForm started");
        Set<User> all = new HashSet<>();
        for (User u : acs.getAllUsers()) {
            all.add(acs.getUserById(u.getId()));
        }
        model.addAttribute("restaurants", rs.getAllRestaurants());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("address", new Address());
        model.addAttribute("roles", ros.getAllRoles());
        model.addAttribute("users", all);
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        User auser = acs.getUserById(Integer.parseInt(userId));
        Role r = ros.getRoleById(Integer.parseInt(restaurantId));
        if (acs.findRolesByUserId(auser.getId()).contains(r)) {
            return "account_form";
        } else {
            ros.addRole(auser.getId(),r.getId());
        }
        acs.saveOrUpdateUser(auser);
        model.addAttribute("user", new User());
        model.addAttribute("restaurants", rs.getAllRestaurants());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("address", new Address());
        model.addAttribute("roles", ros.getAllRoles());
        model.addAttribute("users", acs.getAllUsers());

        logger.info("users added");
        return "account_form";
    }

    @GetMapping("/assign/{userid}")
    public String assignRestaurant(Model model, @PathVariable(name = "userid") String userId, @ModelAttribute("restaurantId") String restaurantId) {
        logger.info("showForm started");
        User auser = acs.getUserById(Integer.parseInt(userId));
        Restaurant r = rs.getRestaurantById(Integer.parseInt(restaurantId));
        auser.setRestaurant(r);
        acs.saveOrUpdateUser(auser);
        model.addAttribute("user", new User());
        model.addAttribute("restaurants", rs.getAllRestaurants());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("address", new Address());
        model.addAttribute("roles", ros.getAllRoles());
        model.addAttribute("users", acs.getAllUsers());
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        logger.info("users added");
        return "account_form";
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
    public String remove(@PathVariable(name = "id") Integer id, Model model) {
        System.out.println("begin posting");
        try {
            acs.deleteUser(id);
            final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = acs.findUserByEmail(currentUserEmail);
            if (user == null) {
                user = new User();
                user.setName("Stranger");
            }
            model.addAttribute("currentUser", user);
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
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        return "account_editform";
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
