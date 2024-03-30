package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.model.Address;
import com.example.demo.model.Order;
import com.example.demo.model.Pizza;
import com.example.demo.service.abs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersMVCController {

    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private OrderService os;

    @Autowired
    private AccountService acs;

    @Autowired
    private PizzaService ps;

    @Autowired
    private SideItemService sis;
    @Autowired
    private AddressService as;

    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("order", new Order());
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("accounts", acs.getAllAccounts());
        model.addAttribute("sideitems", sis.getAllSideItems());
        List<Pizza> pizzas = ps.getAllPizzas();
        pizzas.forEach(pizza -> {
            if (!pizza.getMenu_item()) {
                pizzas.remove(pizza);
            }
        });
        model.addAttribute("pizzas", pizzas);
        logger.info("orders added");
        return "order_form";
    }

    @PostMapping("/create_order")
    public String submitForm(@ModelAttribute("order") Order order) {
        System.out.println("begin posting");
        try {
            System.out.println(order);
            os.createOrder(order);
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
            os.deleteOrder(id);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
        logger.info("showForm started");
        model.addAttribute("order", os.getOrderById(id));
        logger.info("account added");
        return "order_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("order") Order order) {
        System.out.println("begin editing");
        try {
            System.out.println(order);
            os.saveOrUpdateOrder(order);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
