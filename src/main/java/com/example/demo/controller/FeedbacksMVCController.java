package com.example.demo.controller;

import com.example.demo.model.Feedback;
import com.example.demo.service.abs.FeedbackService;
import com.example.demo.service.abs.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedbacks")
public class FeedbacksMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private FeedbackService fs;

    @Autowired
    private OrderService os;


    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("orders", os.getAllOrders());
        model.addAttribute("feedbacks", fs.getAllFeedbacks());
        model.addAttribute("feedback", new Feedback());
        logger.info("feedbacks added");
        return "feedback_form";
    }

    @PostMapping("/create_feedback")
    public String submitForm(@ModelAttribute("feedback") Feedback feedback) {
        System.out.println("begin posting");
        try {
            System.out.println(feedback);
            fs.createFeedback(feedback);
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
            fs.deleteFeedback(id);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable(name = "id") Integer id) {
        logger.info("showForm started");
        model.addAttribute("address", fs.getFeedbackById(id));
        logger.info("restaurant added");
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
