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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrdersMVCController {


    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

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

    @Autowired
    private IngredientService is;

    public List<Pizza> getPizzasForOrder(int orderId) {
        Order order = os.getOrderById(orderId);
        if (order != null) {
            return new ArrayList<>(ps.findPizzasByOrdersId(orderId));
        } else {
            return Collections.emptyList();
        }
    }

    public List<SideItem> getSideItemsForOrder(int orderId) {
        Order order = os.getOrderById(orderId);
        if (order != null) {
            return new ArrayList<>(sis.findSideItemsByOrdersId(orderId));
        } else {
            return Collections.emptyList();
        }
    }


    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("newOrder", new Order());
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("users", acs.getAllUsers());

        Map<Integer, List<Pizza>> pizzasForOrders = new HashMap<>();
        for (OrderDTO order : os.getAllOrders()) {
            List<Pizza> pizzas = getPizzasForOrder(order.getId());
            pizzasForOrders.put(order.getId(), pizzas);
        }
        model.addAttribute("pizzasForOrders", pizzasForOrders);

        Map<Integer, List<SideItem>> sideItemsForOrders = new HashMap<>();
        for (OrderDTO order : os.getAllOrders()) {
            List<SideItem> sideItems = getSideItemsForOrder(order.getId());
            sideItemsForOrders.put(order.getId(), sideItems);
        }
        model.addAttribute("sideItemsForOrders", sideItemsForOrders);


        List<SideItem> sideItems = sis.getAllSideItems();
        List<SideItem> menuSideItems = new ArrayList<>();
        List<SideItem> aSideItems = new ArrayList<>();
        for (SideItem p : sideItems) {
            if (p.getMenu_item()) {
                menuSideItems.add(p);
            } else aSideItems.add(p);
        }
        model.addAttribute("sideitems", menuSideItems);
        model.addAttribute("stage", new Integer(1));
        List<PizzaDTO> pizzas = ps.getAllPizzas();
        List<PizzaDTO> aPizzas = new ArrayList<>();
        List<PizzaDTO> menuPizzas = new ArrayList<>();
        for (PizzaDTO p : pizzas) {
            if (p.getMenu_item()) {
                menuPizzas.add(p);
            } else {
                aPizzas.add(p);
            }
        }
        model.addAttribute("Allpizzas", menuPizzas);
        model.addAttribute("APizzas", aPizzas);
        model.addAttribute("ASideItems", aSideItems);
        logger.info("orders added");
        return "order_form";
    }

    @PostMapping("/create_order_1")
    public String submitForm1(@ModelAttribute("newOrder") Order order, Model model) {
        Map<Integer, List<Pizza>> pizzasForOrders = new HashMap<>();
        for (OrderDTO order1 : os.getAllOrders()) {
            List<Pizza> pizzas = getPizzasForOrder(order1.getId());
            pizzasForOrders.put(order1.getId(), pizzas);
        }
        model.addAttribute("pizzasForOrders", pizzasForOrders);

        Map<Integer, List<SideItem>> sideItemsForOrders = new HashMap<>();
        for (OrderDTO order1 : os.getAllOrders()) {
            List<SideItem> sideItems = getSideItemsForOrder(order1.getId());
            sideItemsForOrders.put(order1.getId(), sideItems);
        }
        model.addAttribute("sideItemsForOrders", sideItemsForOrders);
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
        Map<Integer, List<Pizza>> pizzasForOrders = new HashMap<>();
        for (OrderDTO order1 : os.getAllOrders()) {
            List<Pizza> pizzas = getPizzasForOrder(order1.getId());
            pizzasForOrders.put(order1.getId(), pizzas);
        }
        model.addAttribute("pizzasForOrders", pizzasForOrders);

        Map<Integer, List<SideItem>> sideItemsForOrders = new HashMap<>();
        for (OrderDTO order1 : os.getAllOrders()) {
            List<SideItem> sideItems = getSideItemsForOrder(order1.getId());
            sideItemsForOrders.put(order1.getId(), sideItems);
        }
        model.addAttribute("sideItemsForOrders", sideItemsForOrders);
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
        String orderMap = "";
        model.addAttribute("newOrder", order);
        model.addAttribute("newPizza", new Pizza());
        model.addAttribute("newSideItem", new SideItem());
        model.addAttribute("stage", new Integer(3));
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("users", acs.getAllUsers());
        model.addAttribute("orderMap", orderMap);
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
    public String addItem(@ModelAttribute("newOrder") Order order, @ModelAttribute("orderMap") String orderMap, Model model,
                          @PathVariable String pizzaOrSideItem, @PathVariable String id) {
        Map<Integer, List<Pizza>> pizzasForOrders = new HashMap<>();
        for (OrderDTO order1 : os.getAllOrders()) {
            List<Pizza> pizzas = getPizzasForOrder(order1.getId());
            pizzasForOrders.put(order1.getId(), pizzas);
        }
        model.addAttribute("pizzasForOrders", pizzasForOrders);

        Map<Integer, List<SideItem>> sideItemsForOrders = new HashMap<>();
        for (OrderDTO order1 : os.getAllOrders()) {
            List<SideItem> sideItems = getSideItemsForOrder(order1.getId());
            sideItemsForOrders.put(order1.getId(), sideItems);
        }
        model.addAttribute("sideItemsForOrders", sideItemsForOrders);
        String newStr = "";
        String subStrToFind = pizzaOrSideItem + id;
        int startIndex = orderMap.indexOf(subStrToFind);
        if (orderMap.contains(subStrToFind)) {
            int commaIndex = orderMap.indexOf(",", startIndex);
            int endIndex = orderMap.indexOf("}", commaIndex);

            if (commaIndex != -1 && endIndex != -1) {
                String numStr = orderMap.substring(commaIndex + 1, endIndex);
                int num = Integer.parseInt(numStr);
                num++;
                newStr = orderMap.substring(0, commaIndex + 1) + num + orderMap.substring(endIndex);
            }
        } else {
            newStr = orderMap + "{" + pizzaOrSideItem + id + ",1}";
        }
        HashMap<String, Integer> map = new HashMap<>();
        String[] pairs = newStr.split("[{},]+");

        for (int i = 1; i < pairs.length; i += 2) {
            String key = pairs[i];
            int value = Integer.parseInt(pairs[i + 1]);
            map.put(key, value);
        }
        String currentItems = "";
        for (String key : map.keySet()) {
            int value = map.get(key);
            for (int i = 0; i < value; i++) {
                if (key.startsWith("pizza")) {
                    key = key.replace("pizza", "");
                    currentItems = currentItems + (ps.getPizzaById(Integer.parseInt(key)).getName() + " " + ps.getPizzaById(Integer.parseInt(key)).getPizza_size() + ", ");
                    key = "pizza" + key;
                } else {
                    key = key.replace("sideItem", "");
                    currentItems = currentItems + (sis.getSideItemById(Integer.parseInt(key)).getName() + ", ");
                    key = "sideItem" + key;
                }
            }
        }

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
        model.addAttribute("newOrder", order);
        model.addAttribute("stage", new Integer(3));
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("users", acs.getAllUsers());
        model.addAttribute("Allpizzas", menuPizzas);
        model.addAttribute("orderMap", newStr);
        model.addAttribute("currentItems", currentItems);
        return "order_form";
    }

    @PostMapping("/remove_item/{pizzaOrSideItem}/{id}")
    public String removeItem(@ModelAttribute("newOrder") Order order, @ModelAttribute("orderMap") String orderMap, Model model,
                             @PathVariable String pizzaOrSideItem, @PathVariable String id) {
        Map<Integer, List<Pizza>> pizzasForOrders = new HashMap<>();
        for (OrderDTO order1 : os.getAllOrders()) {
            List<Pizza> pizzas = getPizzasForOrder(order1.getId());
            pizzasForOrders.put(order1.getId(), pizzas);
        }
        model.addAttribute("pizzasForOrders", pizzasForOrders);

        Map<Integer, List<SideItem>> sideItemsForOrders = new HashMap<>();
        for (OrderDTO order1 : os.getAllOrders()) {
            List<SideItem> sideItems = getSideItemsForOrder(order1.getId());
            sideItemsForOrders.put(order1.getId(), sideItems);
        }
        model.addAttribute("sideItemsForOrders", sideItemsForOrders);
        String newStr = "";
        String subStrToFind = pizzaOrSideItem + id;
        int startIndex = orderMap.indexOf(subStrToFind);
        if (orderMap.contains(subStrToFind)) {
            int commaIndex = orderMap.indexOf(",", startIndex);
            int endIndex = orderMap.indexOf("}", commaIndex);
            if (commaIndex != -1 && endIndex != -1) {
                String numStr = orderMap.substring(commaIndex + 1, endIndex);
                int num = Integer.parseInt(numStr);
                num--;
                if (num == 0) {
                    newStr = orderMap.replace("{" + subStrToFind + ",1}", "");
                } else {
                    newStr = orderMap.substring(0, commaIndex + 1) + num + orderMap.substring(endIndex);
                }
            }
        } else {
            newStr = orderMap;
        }
        HashMap<String, Integer> map = new HashMap<>();
        String[] pairs = newStr.split("[{},]+");

        for (int i = 1; i < pairs.length; i += 2) {
            String key = pairs[i];
            int value = Integer.parseInt(pairs[i + 1]);
            map.put(key, value);
        }
        String currentItems = "";
        for (String key : map.keySet()) {
            int value = map.get(key);
            for (int i = 0; i < value; i++) {
                if (key.startsWith("pizza")) {
                    key = key.replace("pizza", "");
                    currentItems = currentItems + (ps.getPizzaById(Integer.parseInt(key)).getName() + " " + ps.getPizzaById(Integer.parseInt(key)).getPizza_size() + ", ");
                    key = "pizza" + key;
                } else {
                    key = key.replace("sideItem", "");
                    currentItems = currentItems + (sis.getSideItemById(Integer.parseInt(key)).getName() + ", ");
                    key = "sideItem" + key;
                }
            }
        }

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
        model.addAttribute("newOrder", order);
        model.addAttribute("stage", new Integer(3));
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("users", acs.getAllUsers());
        model.addAttribute("Allpizzas", menuPizzas);
        model.addAttribute("orderMap", newStr);
        model.addAttribute("currentItems", currentItems);
        return "order_form";
    }

    @Transactional
    @PostMapping("/create_order_3")
    public String addItem(@ModelAttribute("newOrder") Order order, @ModelAttribute("orderMap") String orderMap) {
        Timestamp tst = new Timestamp((System.currentTimeMillis()));
        Set<Order> orders = new HashSet<>();
        order.setCreated(tst);
        os.createOrder(os.mapEntityToDto(order));

        List<OrderDTO> allOrders = os.getAllOrders();
        OrderDTO currentOrder = new OrderDTO();
        for (OrderDTO orderDTO : allOrders) {
            if (orderDTO.getCreated() != null && orderDTO.getCreated().equals(tst)) {
                currentOrder = orderDTO;
            }
        }

        orders.add(os.mapDtoToEntity(currentOrder, new Order()));

        HashMap<String, Integer> map = new HashMap<>();
        String[] pairs = orderMap.split("[{},]+");
        for (int i = 1; i < pairs.length; i += 2) {
            String key = pairs[i];
            int value = Integer.parseInt(pairs[i + 1]);
            map.put(key, value);
        }
        List<Pizza> currentPizzas = new ArrayList<>();
        List<SideItem> currentItems = new ArrayList<>();
        for (String key : map.keySet()) {
            int value = map.get(key);
            for (int i = 0; i < value; i++) {
                if (key.startsWith("pizza")) {
                    key = key.replace("pizza", "");
                    Pizza pizza = new Pizza();
                    Pizza oldPizza = ps.getPizzaById(Integer.parseInt(key));
                    pizza.setIngredients(oldPizza.getIngredients());
                    pizza.setPizza_size(oldPizza.getPizza_size());
                    pizza.setMenu_item(false);
                    pizza.setOrders(orders);
                    pizza.setPrice(oldPizza.getPrice());
                    pizza.setId(null);
                    pizza.setName(order.getUser().getName() + (i + 1) + oldPizza.getName());
                    ps.createPizza(ps.mapEntityToDto(pizza));
                    currentPizzas.add(pizza);
                    key = "pizza" + key;
                } else {
                    key = key.replace("sideItem", "");
                    SideItem sideItem = new SideItem();
                    SideItem oldSideItem = sis.getSideItemById(Integer.parseInt(key));
                    sideItem.setOrders(orders);
                    sideItem.setName(order.getUser().getName() + (i + 1) + oldSideItem.getName());
                    sideItem.setMenu_item(false);
                    sideItem.setId(null);
                    sideItem.setPrice(oldSideItem.getPrice());
                    sis.createSideItem(sideItem);
                    currentItems.add(sideItem);
                    key = "sideItem" + key;
                }
                List<SideItem> all = sis.getAllSideItems();
                List<PizzaDTO> all1 = ps.getAllPizzas();
                OrderDTO finalCurrentOrder = currentOrder;
                all.forEach(sideItem -> {
                    if (sideItem.getName().startsWith(finalCurrentOrder.getUser().getName()) && os.findOrdersBySide_itemsId(sideItem.getId()).isEmpty()) {
                        os.addSideItemToOrder(finalCurrentOrder.getId(), sideItem.getId());
                    }
                });
                all1.forEach(pizzaDTO -> {
                    if (pizzaDTO.getName().startsWith(finalCurrentOrder.getUser().getName()) && os.findOrdersByPizzasId(pizzaDTO.getId()).isEmpty()) {
                        os.addPizzaToOrder(finalCurrentOrder.getId(), pizzaDTO.getId());
                    }
                });
            }
        }
        return "request_success";
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
