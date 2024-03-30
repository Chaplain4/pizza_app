package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.service.abs.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/roles")
public class RolesMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private RoleService rs;


    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("roles", rs.getAllRoles());
        model.addAttribute("role", new Role());
        logger.info("roles added");
        return "role_form";
    }

    @PostMapping("/create_role")
    public String submitForm(@ModelAttribute("role") Role role) {
        System.out.println("begin posting");
        try {
            System.out.println(role);
            rs.createRole(role);
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
            rs.deleteRole(id);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
        logger.info("showForm started");
        model.addAttribute("role", rs.getRoleById(id));
        logger.info("role added");
        return "role_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("role") Role role) {
        System.out.println("begin editing");
        try {
            System.out.println(role);
            rs.saveOrUpdateRole(role);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
