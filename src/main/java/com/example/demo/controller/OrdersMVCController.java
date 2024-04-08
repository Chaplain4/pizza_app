package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.*;
import com.example.demo.repository.PizzaRepository;
import com.example.demo.repository.SideItemRepository;
import com.example.demo.service.abs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrdersMVCController {


    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private PizzaRepository pr;

    @Autowired
    private SideItemRepository sir;

    @Autowired
    private OrderService os;

    @Autowired
    private UserService acs;

    @Autowired
    private PizzaService ps;

    @Autowired
    private SideItemService sis;

    @Autowired
    private RestaurantService rs;
    @Autowired
    private AddressService as;

    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("newOrder", new Order());
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("users", acs.getAllUsers());
        List<SideItem> sideItems = sis.getAllSideItems();
        List<SideItem> menuSideItems = new ArrayList<>();
        for (SideItem p : sideItems) {
            if (p.getMenu_item()) {
                menuSideItems.add(p);
            }
        }
        model.addAttribute("sideitems", menuSideItems);
        model.addAttribute("stage", new Integer(1));
        List<PizzaDTO> pizzas = ps.getAllPizzas();
        List<PizzaDTO> menuPizzas = new ArrayList<>();
        for (PizzaDTO p : pizzas) {
            if (p.getMenu_item()) {
                menuPizzas.add(p);
            }
        }
        model.addAttribute("Allpizzas", menuPizzas);
        logger.info("orders added");
        return "order_form";
    }

    @PostMapping("/create_order_1")
    public String submitForm1(@ModelAttribute("newOrder") Order order, Model model) {
        System.out.println("begin posting");
        if (order.getTo_deliver() == null) {
            order.setTo_deliver(false);
        }
        model.addAttribute("newOrder", order);
        model.addAttribute("stage", new Integer(2));
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("restaurants", rs.getAllRestaurants());
        if (order.getUser() != null) {
            Set<Address> addressSet = new HashSet<>();
            for (Address a : as.getAllAddresses()) {
                for (User u : a.getUsers()) {
                    if (u.getId().equals(order.getUser().getId())) {
                        addressSet.add(a);
                    }
                }
            }
            model.addAttribute("addresses", addressSet);
        } else model.addAttribute("addresses", new HashSet<>());
        model.addAttribute("users", acs.getAllUsers());
        model.addAttribute("sideitems", sis.getAllSideItems());
        return "order_form";
    }

    @PostMapping("/create_order_2")
    public String submitForm2(@ModelAttribute("newOrder") Order order, @ModelAttribute("newAddress") Address address, Model model) {
        System.out.println("begin posting");
        if (order.getTo_deliver() && order.getAddress() == null) {
            order.setAddress(address);
            if (order.getUser() != null) {
                User user = order.getUser();
                Set<User> users = new HashSet<>();
                Set<Address> addresses = new HashSet<>();
                users.add(user);
                address.setUsers(users);
                as.createAddress(address);
                address.setUsers(null);
                addresses.add(address);
                user.setAddresses(addresses);
                acs.saveOrUpdateUser(user);
            }
        }
        model.addAttribute("newOrder", order);
        model.addAttribute("stage", new Integer(3));
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("users", acs.getAllUsers());
        List<SideItem> sideItems = sis.getAllSideItems();
        List<SideItem> menuSideItems = new ArrayList<>();
        for (SideItem p : sideItems) {
            if (p.getMenu_item()) {
                menuSideItems.add(p);
            }
        }
        model.addAttribute("sideitems", menuSideItems);
        List<PizzaDTO> pizzas = ps.getAllPizzas();
        List<PizzaDTO> menuPizzas = new ArrayList<>();
        for (PizzaDTO p : pizzas) {
            if (p.getMenu_item()) {
                menuPizzas.add(p);
            }
        }
        model.addAttribute("Allpizzas", menuPizzas);
        return "order_form";
    }

    @PostMapping("/add_item/{pizzaOrSideItem}/{id}")
    public String addItem(@ModelAttribute("newOrder") Order order, Model model, @PathVariable String pizzaOrSideItem, @PathVariable String id) {
        if (pizzaOrSideItem.equals("pizza")) {
            Pizza pizza = ps.getPizzaById(Integer.parseInt(id));
            Pizza newPizza = new Pizza();
            newPizza.setMenu_item(false);
            newPizza.setPrice(pizza.getPrice());
            newPizza.setIngredients(pizza.getIngredients());
            newPizza.setName(pizza.getName());
            newPizza.setPizza_size(pizza.getPizza_size());
            PizzaDTO newPizzaDTO = ps.mapEntityToDto(newPizza);
            ps.createPizza(newPizzaDTO);
            Set<Pizza> pizzas = new HashSet<>();
            if (order.getPizzas() != null) {
                pizzas = order.getPizzas();
            }
            pizzas.add(pizza);
            order.setPizzas(pizzas);
        }
        if (pizzaOrSideItem.equals("sideItem")) {
            SideItem sideItem = sis.getSideItemById(Integer.parseInt(id));
            sideItem.setMenu_item(false);
            sideItem.setId(null);
            sideItem.setOrders(null);
            sis.createSideItem(sideItem);
            Set<SideItem> sideItems = order.getSide_items();
            sideItems.add(sideItem);
            order.setSide_items(sideItems);
        }
        model.addAttribute("newOrder", order);
        model.addAttribute("stage", new Integer(3));
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("users", acs.getAllUsers());
        List<SideItem> sideItems = sis.getAllSideItems();
        List<SideItem> menuSideItems = new ArrayList<>();
        for (SideItem p : sideItems) {
            if (p.getMenu_item()) {
                menuSideItems.add(p);
            }
        }
        model.addAttribute("sideitems", menuSideItems);
        List<PizzaDTO> pizzas = ps.getAllPizzas();
        List<PizzaDTO> menuPizzas = new ArrayList<>();
        for (PizzaDTO p : pizzas) {
            if (p.getMenu_item()) {
                menuPizzas.add(p);
            }
        }
        model.addAttribute("Allpizzas", menuPizzas);
        return "order_form";
    }

    @GetMapping("/delete/{id}")
    public String remove(@PathVariable(name = "id") Integer id) {
//        System.out.println("begin posting");
//        try {
//            os.deleteOrder(id);
//            return "request_success";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return "request_fail";
    }

    @ModelAttribute("newAddress")
    public Address getNewAddress() {
        return new Address();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
//        logger.info("showForm started");
//        model.addAttribute("order", os.getOrderById(id));
//        logger.info("user added");
        return "order_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("order") Order order) {
        System.out.println("begin editing");
//        try {
//            System.out.println(order);
//            os.saveOrUpdateOrder(order);
//            return "request_success";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return "request_fail";
    }
}
