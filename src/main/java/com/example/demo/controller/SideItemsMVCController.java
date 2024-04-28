package com.example.demo.controller;

import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.Role;
import com.example.demo.model.SideItem;
import com.example.demo.model.User;
import com.example.demo.service.abs.SideItemService;
import com.example.demo.service.abs.UserService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/sideitems")
public class SideItemsMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private SideItemService sis;

    @Autowired
    UserService acs;

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
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        List<SideItem> sideItems = new ArrayList<>();
        for (SideItem p : sis.getAllSideItems()) {
            if (p.getMenu_item()!=null && p.getMenu_item()) {
                sideItems.add(p);
            }
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("sideitems", sideItems);
        model.addAttribute("sideitem", new SideItem());
        logger.info("side items added");
        return "sideitem_form";
    }

    @PostMapping("/create_sideitem")
    public String submitForm(@ModelAttribute("sideitem") SideItem sideItem) {
        System.out.println("begin posting");
        try {
            System.out.println(sideItem);
            sis.createSideItem(sideItem);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }

    @GetMapping("/delete/{id}")
    public String remove(@PathVariable(name = "id") Integer id, Model model) {
        System.out.println("begin posting");
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        try {
            sis.deleteSideItem(id);
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
        model.addAttribute("sideitem", sis.getSideItemById(id));
        logger.info("side item added");
        return "sideitem_editform";
    }

    @PostMapping("/edit")
    public String submitEditForm(@ModelAttribute("sideitem") SideItem sideItem) {
        System.out.println("begin editing");
        try {
            System.out.println(sideItem);
            sis.saveOrUpdateSideItem(sideItem);
            return "request_success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request_fail";
    }
}
