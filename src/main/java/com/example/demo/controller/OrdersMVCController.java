package com.example.demo.controller;


import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.*;
import com.example.demo.service.abs.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/orders")
public class OrdersMVCController {

    @PersistenceContext
    private EntityManager entityManager;


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

    @ModelAttribute("currentRoles")
    public Set<String> currentRoles() {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            return new HashSet<>();
        } else
            return getStringsFromRoles(new HashSet<>(acs.findRolesByUserId(user.getId())));
    }

    public Set<String> getStringsFromRoles(Set<Role> roles) {
        Set<String> result = new HashSet<>();
        roles.forEach(role -> {
            result.add(role.getName());
        });
        return result;
    }

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



    @GetMapping("/boss")
    public String boss(Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        for (OrderDTO o : allOrders) {
            if (o.getRestaurant() != null) {
                if (o.getRestaurant().equals(user.getRestaurant())) {
                    ordersInProgress.add(o);
                }
            }
        }
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "boss_form";
    }

    @GetMapping("/ceo")
    public String ceoForm(Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("newOrder", NewOrder);
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
        return "ceo_form";
    }

    @GetMapping("/waiter")
    public String waiter(Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        int restId = user.getRestaurant().getId();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        allOrders.forEach(orderDTO -> {
            if (orderDTO.getRestaurant() != null) {
                if (!orderDTO.getComment().endsWith("CANCELLED") && orderDTO.getAssembled() != null && !orderDTO.getTo_deliver() && orderDTO.getDelivered() == null && orderDTO.getRestaurant().getId().equals(restId)) {
                    ordersInProgress.add(orderDTO);
                }
            }
        });
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "waiter_form";
    }


    @GetMapping("/delivery")
    public String delivery(Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        allOrders.forEach(orderDTO -> {
            if (!orderDTO.getComment().endsWith("CANCELLED") && orderDTO.getAssembled() != null && orderDTO.getTo_deliver() && orderDTO.getDelivered() == null) {
                ordersInProgress.add(orderDTO);
            }
        });
        Set<String> maps = new HashSet<>();
        ordersInProgress.forEach(orderDTO -> {
            try {
                maps.add("https://maps.googleapis.com/maps/api/staticmap?center=" + URLEncoder.encode("Минск, " + orderDTO.getAddress().getStreet(), "UTF-8") + "&zoom=14&size=600x300&markers=color:red%7Clabel:A%7C${" + URLEncoder.encode("Минск, " + orderDTO.getAddress().getStreet(), "UTF-8") + "}&key=AIzaSyAgmvuDWjcLLnb-gzoAMkQAtYdkLpGN62Y")
                ;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
        model.addAttribute("maps", maps);
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "delivery_form";
    }

    @GetMapping("/cook")
    public String cook(Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        int restId = user.getRestaurant().getId();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        allOrders.forEach(orderDTO -> {
            if (!orderDTO.getComment().endsWith("CANCELLED") && orderDTO.getAssembled() == null && orderDTO.getConfirmed() != null && (orderDTO.getRestaurant() == null || orderDTO.getRestaurant().getId().equals(restId))) {
                ordersInProgress.add(orderDTO);
            }
        });
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "cook_form";
    }

    @GetMapping("/callOp")
    public String callOp(Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        allOrders.forEach(orderDTO -> {
            if (!orderDTO.getComment().endsWith("CANCELLED")) {
                if (orderDTO.getDelivered() != null || !orderDTO.getComment().endsWith("DELIVERED")) {
                    ordersInProgress.add(orderDTO);
                }
            }
        });
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "callOp_form";
    }

    @GetMapping("/service/{id}")
    public String service(Model model, @PathVariable(name = "id") String id) {
        Order corder = os.getOrderById(Integer.parseInt(id));
        corder.setComment(corder.getComment() + " DELIVERED");
        corder.setDelivered(new Timestamp(System.currentTimeMillis()));
        os.saveOrUpdateOrder(corder.getId(), os.mapEntityToDto(corder));
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        int restId = user.getRestaurant().getId();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        allOrders.forEach(orderDTO -> {
            if (!orderDTO.getComment().endsWith("CANCELLED") && orderDTO.getAssembled() == null && orderDTO.getConfirmed() != null && (orderDTO.getRestaurant() == null || orderDTO.getRestaurant().getId().equals(restId))) {
                ordersInProgress.add(orderDTO);
            }
        });
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "waiter_form";
    }

    @GetMapping("/deliver/{id}")
    public String deliver(Model model, @PathVariable(name = "id") String id) {
        Order corder = os.getOrderById(Integer.parseInt(id));
        corder.setComment(corder.getComment() + " DELIVERED");
        corder.setDelivered(new Timestamp(System.currentTimeMillis()));
        os.saveOrUpdateOrder(corder.getId(), os.mapEntityToDto(corder));
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        int restId = user.getRestaurant().getId();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        allOrders.forEach(orderDTO -> {
            if (!orderDTO.getComment().endsWith("CANCELLED") && orderDTO.getAssembled() == null && orderDTO.getConfirmed() != null && (orderDTO.getRestaurant() == null || orderDTO.getRestaurant().getId().equals(restId))) {
                ordersInProgress.add(orderDTO);
            }
        });
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "delivery_form";
    }

    @GetMapping("/assemble/{id}")
    public String assemble(Model model, @PathVariable(name = "id") String id) {
        Order corder = os.getOrderById(Integer.parseInt(id));
        corder.setComment(corder.getComment() + " ASSEMBLED");
        corder.setAssembled(new Timestamp(System.currentTimeMillis()));
        os.saveOrUpdateOrder(corder.getId(), os.mapEntityToDto(corder));
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        int restId = user.getRestaurant().getId();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        allOrders.forEach(orderDTO -> {
            if (!orderDTO.getComment().endsWith("CANCELLED") && orderDTO.getAssembled() == null && orderDTO.getConfirmed() != null && (orderDTO.getRestaurant() == null || orderDTO.getRestaurant().getId().equals(restId))) {
                ordersInProgress.add(orderDTO);
            }
        });
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "cook_form";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(Model model, @PathVariable(name = "id") String id) {
        Order corder = os.getOrderById(Integer.parseInt(id));
        corder.setComment(corder.getComment() + " CANCELLED");
        os.saveOrUpdateOrder(corder.getId(), os.mapEntityToDto(corder));
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        allOrders.forEach(orderDTO -> {
            if (!orderDTO.getComment().endsWith("CANCELLED")) {
                if (orderDTO.getDelivered() != null || !orderDTO.getComment().endsWith("DELIVERED")) {
                    ordersInProgress.add(orderDTO);
                }
            }
        });
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "callOp_form";
    }

    @GetMapping("/confirm/{id}")
    public String confirm(Model model, @PathVariable(name = "id") String id) {
        Order corder = os.getOrderById(Integer.parseInt(id));
        corder.setComment(corder.getComment() + " CONFIRMED");
        corder.setConfirmed(new Timestamp(System.currentTimeMillis()));
        os.saveOrUpdateOrder(corder.getId(), os.mapEntityToDto(corder));
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<OrderDTO> allOrders = os.getAllOrders();
        List<OrderDTO> ordersInProgress = new ArrayList<>();
        allOrders.forEach(orderDTO -> {
            if (!orderDTO.getComment().endsWith("CANCELLED")) {
                if (orderDTO.getDelivered() != null || !orderDTO.getComment().endsWith("DELIVERED")) {
                    ordersInProgress.add(orderDTO);
                }
            }
        });
        model.addAttribute("newOrder", NewOrder);
        model.addAttribute("orders", ordersInProgress);
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
        return "callOp_form";
    }

    @GetMapping("/list")
    public String showForm(Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        logger.info("showForm started");
        Order NewOrder = new Order();
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            NewOrder.setUser(user);
        } else {
            NewOrder.setUser(user);
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("newOrder", NewOrder);
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
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        if (user != null) {
            order.setUser(user);
            Set<Address> addressSet = new HashSet<>();
            for (Address a : as.getAllAddresses()) {
                if (a.getUsers().contains(user)) {
                    addressSet.add(a);
                }
            }
            model.addAttribute("addresses", addressSet);
        } else model.addAttribute("addresses", new HashSet<>());
        model.addAttribute("currentUser", user);
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

        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            order.setUser(user);
        }
        model.addAttribute("currentUser", user);
        String customPizzas = new String();
        for (PizzaDTO pizza : ps.getAllPizzas()) {
            if (pizza.getName().contains(user.getName()) && os.findOrdersByPizzasId(pizza.getId()).isEmpty()) {
                customPizzas = customPizzas + pizza.getId() + pizza.getName() + " ";
            }
        }

        List<Ingredient> ingredients1 = is.getAllIngredients();
        List<Ingredient> allIngredients = new ArrayList<>();
        for (Ingredient i : ingredients1) {
            if (i.getPizza_size().equals("S") && !i.getName().equals("Thin dough") && !i.getName().equals("Red sauce")) {
                allIngredients.add(i);
            }
        }

        String orderMap = "";
        model.addAttribute("allIngredients", allIngredients);
        model.addAttribute("newOrder", order);
        model.addAttribute("newPizza", new Pizza());
        model.addAttribute("newSideItem", new SideItem());
        model.addAttribute("stage", new Integer(3));
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("addresses", as.getAllAddresses());
        model.addAttribute("users", acs.getAllUsers());
        model.addAttribute("orderMap", orderMap);
        model.addAttribute("customPizzas", customPizzas);
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
        model.addAttribute("customPizza", new PizzaDTO());
        return "order_form";
    }

    @PostMapping("/add_item/customPizza")
    public String addCustomItem(@ModelAttribute("newOrder") Order order,
                                @ModelAttribute("orderMap") String orderMap, @ModelAttribute("pizza_size") String pizza_size,
                                @ModelAttribute("ingredients") Set<Ingredient> ingredients, Model model) {
        System.out.println("begin posting");
        PizzaDTO customPizza = new PizzaDTO();
        customPizza.setPizza_size(pizza_size);
        customPizza.setIngredients(ingredients);
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
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            order.setUser(user);
        }
        model.addAttribute("currentUser", user);
        List<Ingredient> all = is.getAllIngredients();
        Set<Ingredient> Oldingredients = customPizza.getIngredients();
        Set<Ingredient> newIngredients = new HashSet<>();
        newIngredients.add(is.getIngredientById(1));
        newIngredients.add(is.getIngredientById(4));
        Oldingredients.forEach(ingredient -> {
            if (ingredient.getPizza_size().equals(customPizza.getPizza_size())) {
                newIngredients.add(ingredient);
            } else {
                all.forEach(ingredient1 -> {
                    if (ingredient1.getName().equals(ingredient.getName()) && ingredient1.getPizza_size().equals(customPizza.getPizza_size())) {
                        newIngredients.add(ingredient1);
                    }
                });
            }
        });
        customPizza.setIngredients(newIngredients);
        customPizza.setName("{" + user.getName() + "Custom Pizza}");
        customPizza.setMenu_item(false);
        Double price = 0.0;
        for (Ingredient i : newIngredients) {
            price += i.getPrice();
        }
        customPizza.setPrice(price);
        PizzaDTO newCustomPizza = ps.createPizza(customPizza);
        String customPizzas = new String();
        for (PizzaDTO pizza : ps.getAllPizzas()) {
            if (pizza.getName().contains(user.getName()) && os.findOrdersByPizzasId(pizza.getId()).isEmpty()) {
                customPizzas = customPizzas + pizza.getId() + pizza.getName() + " ";
            }
        }
        String newStr = orderMap;
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

        List<Ingredient> ingredients1 = is.getAllIngredients();
        List<Ingredient> allIngredients = new ArrayList<>();
        for (Ingredient i : ingredients1) {
            if (i.getPizza_size().equals("S") && !i.getName().equals("Thin dough") && !i.getName().equals("Red sauce")) {
                allIngredients.add(i);
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
        model.addAttribute("customPizzas", customPizzas);
        model.addAttribute("customPizza", new PizzaDTO());
        model.addAttribute("allIngredients", allIngredients);
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
        String newStr = orderMap + "{customPizza}";
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

        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        String customPizzas = new String();
        for (PizzaDTO pizza : ps.getAllPizzas()) {
            if (pizza.getName().contains(user.getName()) && os.findOrdersByPizzasId(pizza.getId()).isEmpty()) {
                customPizzas = customPizzas + pizza.getId() + pizza.getName() + " ";
            }
        }

        List<Ingredient> ingredients1 = is.getAllIngredients();
        List<Ingredient> allIngredients = new ArrayList<>();
        for (Ingredient i : ingredients1) {
            if (i.getPizza_size().equals("S") && !i.getName().equals("Thin dough") && !i.getName().equals("Red sauce")) {
                allIngredients.add(i);
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
        model.addAttribute("customPizzas", customPizzas);
        model.addAttribute("customPizza", new PizzaDTO());
        model.addAttribute("allIngredients", allIngredients);
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

        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        String customPizzas = new String();
        for (PizzaDTO pizza : ps.getAllPizzas()) {
            if (pizza.getName().contains(user.getName()) && os.findOrdersByPizzasId(pizza.getId()).isEmpty()) {
                customPizzas = customPizzas + pizza.getId() + pizza.getName() + " ";
            }
        }

        List<Ingredient> ingredients1 = is.getAllIngredients();
        List<Ingredient> allIngredients = new ArrayList<>();
        for (Ingredient i : ingredients1) {
            if (i.getPizza_size().equals("S") && !i.getName().equals("Thin dough") && !i.getName().equals("Red sauce")) {
                allIngredients.add(i);
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
        model.addAttribute("customPizzas", customPizzas);
        model.addAttribute("customPizza", new PizzaDTO());
        model.addAttribute("allIngredients", allIngredients);
        return "order_form";
    }

    @Transactional
    @PostMapping("/create_order_3")
    public String addItem(@ModelAttribute("newOrder") Order order, @ModelAttribute("orderMap") String orderMap, Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
            user.setIsActivated(false);
        }
        model.addAttribute("currentUser", user);
        Boolean isActivated = false;
        if (user.getIsActivated()) {
            isActivated = user.getIsActivated();
        }
        Timestamp tst = new Timestamp((System.currentTimeMillis()));
        Set<Order> orders = new HashSet<>();
        order.setCreated(tst);
        order.setComment(order.getComment() + " CREATED");
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
        if (order.getUser() == null) {
            user.setName("Stranger");
            order.setUser(user);
        }
        Integer bonus = 0;
        List<Pizza> currentPizzas = new ArrayList<>();
        List<SideItem> currentItems = new ArrayList<>();
        for (String key : map.keySet()) {
            int value = map.get(key);
            for (int i = 0; i < value; i++) {
                if (key.startsWith("pizza")) {
                    key = key.replace("pizza", "");
                    Pizza pizza = new Pizza();
                    Pizza oldPizza = ps.getPizzaById(Integer.parseInt(key));
                    pizza.setPizza_size(oldPizza.getPizza_size());
                    pizza.setMenu_item(false);
                    pizza.setOrders(orders);
                    pizza.setPrice(oldPizza.getPrice());
                    bonus += (Integer) (int) (oldPizza.getPrice() / 10);
                    pizza.setId(null);
                    pizza.setName(order.getUser().getName() + (i + 1) + oldPizza.getName());
                    pizza.setIngredients(new HashSet<>(is.findIngredientsByPizzasId(oldPizza.getId())));
                    PizzaDTO newPizza = ps.createPizza(ps.mapEntityToDto(pizza));
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
                    bonus += (Integer) (int) (oldSideItem.getPrice() / 10);
                    sis.createSideItem(sideItem);
                    currentItems.add(sideItem);
                    key = "sideItem" + key;
                }
                List<SideItem> all = sis.getAllSideItems();
                List<PizzaDTO> all1 = ps.getAllPizzas();
                OrderDTO finalCurrentOrder = currentOrder;
                if (finalCurrentOrder.getUser() == null) {
                    user.setName("Stranger");
                    finalCurrentOrder.setUser(user);
                }
                all.forEach(sideItem -> {
                    if (sideItem.getName().startsWith(finalCurrentOrder.getUser().getName()) && os.findOrdersBySide_itemsId(sideItem.getId()).isEmpty()) {
                        os.addSideItemToOrder(finalCurrentOrder.getId(), sideItem.getId());
                        String newName = sideItem.getName().replace(finalCurrentOrder.getUser().getName(), "").replaceAll("\\d", "");
                        sideItem.setName(newName);
                        sis.saveOrUpdateSideItem(sideItem);
                    }
                });
                all1.forEach(pizzaDTO -> {
                    if (pizzaDTO.getName().startsWith(finalCurrentOrder.getUser().getName()) && os.findOrdersByPizzasId(pizzaDTO.getId()).isEmpty()) {
                        os.addPizzaToOrder(finalCurrentOrder.getId(), pizzaDTO.getId());
                        String newName = pizzaDTO.getName().replace(finalCurrentOrder.getUser().getName(), "").replaceAll("\\d", "");
                        pizzaDTO.setName(newName);
                        ps.saveOrUpdatePizza(pizzaDTO.getId(), pizzaDTO);
                    }
                });
                User user1 = finalCurrentOrder.getUser();
            }
        }


        if (user == null) {
            user = new User();
            user.setName("Stranger");
            order.setUser(user);
        }

        for (PizzaDTO pizza : ps.getAllPizzas()) {
            if (pizza.getName().contains("{" + user.getName() + "Custom Pizza}") && os.findOrdersByPizzasId(pizza.getId()).isEmpty()) {
                pizza.setName("Custom Pizza");
                bonus += (Integer) (int) (pizza.getPrice() / 10);
                os.addPizzaToOrder(currentOrder.getId(), pizza.getId());
            }
        }
        if (user.getBonus_coins() == null) {
            user.setBonus_coins(0);
        }
        if (isActivated && (user.getBonus_coins() < bonus * 10)) {
            user.setBonus_coins(user.getBonus_coins() + bonus);
            acs.saveOrUpdateUser(user);
        } else {
            user.setBonus_coins(user.getBonus_coins() - bonus * 10);
            acs.saveOrUpdateUser(user);
            currentOrder.setComment("BONUS! " + currentOrder.getComment());
            os.saveOrUpdateOrder(currentOrder.getId(), currentOrder);
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
