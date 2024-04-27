package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.model.*;
import com.example.demo.service.abs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/feedbacks")
public class FeedbacksMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private FeedbackService fs;

    @Autowired
    private OrderService os;

    @Autowired
    private UserService acs;

    @Autowired
    private PizzaService ps;

    @Autowired
    private SideItemService sis;

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
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        final User user1 = user;
        model.addAttribute("currentUser", user);
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
        List<OrderDTO> orderList = new ArrayList<>();
        List <Feedback> feedbacks = fs.getAllFeedbacks();
        List <Integer> ids = new ArrayList<>();
        feedbacks.forEach(feedback -> {
            if (feedback.getOrder().getId() != null) {
                ids.add(feedback.getOrder().getId());
            }
        });
        os.getAllOrders().forEach(orderDTO -> {
            if (os.getOrderById(orderDTO.getId()).getUser() != null && os.getOrderById(orderDTO.getId()).getUser().getId().equals(user1.getId()) && !ids.contains(orderDTO.getId()) && orderDTO.getDelivered() != null) {
                orderList.add(orderDTO);
            }
        });
        model.addAttribute("orders", orderList);
        model.addAttribute("feedback", new Feedback());
        logger.info("feedbacks added");
        return "feedback_form";
    }

    @GetMapping("/add/{orderId}")
    public String openEditForm(@PathVariable String orderId, Model model) {
        logger.info("editForm started");
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        Feedback feedback = new Feedback();
        feedback.setUser(acs.findUserByEmail(currentUserEmail));
        feedback.setOrder(os.getOrderById(Integer.parseInt(orderId)));
        if (!Objects.equals(acs.findUserByEmail(currentUserEmail).getId(), os.getOrderById(Integer.parseInt(orderId)).getUser().getId())) {
            return "request_fail";
        }
        model.addAttribute("feedback", feedback);
        return "feedback_editform";
    }

    @PostMapping("/create_feedback")
    public String submitForm(@ModelAttribute("feedback") Feedback feedback) {
        System.out.println("begin posting");
        try {
            feedback.setCreated(new Timestamp(System.currentTimeMillis()));

            System.out.println(feedback);
            fs.createFeedback(feedback);
//            Order order = os.getOrderById(feedback.getOrder().getId());
//            order.setFeedback(feedback);
//            os.saveOrUpdateOrder(order.getId(),os.mapEntityToDto(order));
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
            fs.deleteFeedback(id);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        logger.info("showForm started");
        model.addAttribute("feedback", fs.getFeedbackById(id));
        logger.info("feedback added");
        return "feedback_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("feedback") Feedback feedback) {
        System.out.println("begin editing");
        try {
            System.out.println(feedback);
            fs.saveOrUpdateFeedback(feedback);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
