package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.model.Address;
import com.example.demo.service.abs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class AccountsMVCController {

    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private AccountService acs;
    @Autowired
    private RestaurantService rs;

    @Autowired
    private RoleService ros;

    @Autowired
    private AddressService as;

    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("account", new Account());
        model.addAttribute("restaurants", rs.getAllRestaurants());
        model.addAttribute("address", new Address());
        model.addAttribute("roles", ros.getAllRoles());
        logger.info("restaurants added");
        return "account_form";
    }

    @PostMapping("/create_account")
    public String submitForm(@ModelAttribute("account") Account account) {
        System.out.println("begin posting");
        try {
            System.out.println(account);
            account.getAddresses().forEach(address -> {
                as.createAddress(address);
            });
            acs.createAccount(account);
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
            acs.deleteAccount(id);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
        logger.info("showForm started");
        model.addAttribute("account", acs.getAccountById(id));
        logger.info("account added");
        return "account_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("account") Account account) {
        System.out.println("begin editing");
        try {
            System.out.println(account);
            account.getAddresses().forEach(address -> {
                as.saveOrUpdateAddress(address);
            });
            acs.saveOrUpdateAccount(account);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
