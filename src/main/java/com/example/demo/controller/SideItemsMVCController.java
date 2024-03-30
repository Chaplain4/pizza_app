package com.example.demo.controller;

import com.example.demo.model.SideItem;
import com.example.demo.service.abs.SideItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sideitems")
public class SideItemsMVCController {
    Logger logger = LoggerFactory.getLogger(RestaurantsMVCController.class);

    @Autowired
    private SideItemService sis;

    @GetMapping("/list")
    public String showForm(Model model) {
        logger.info("showForm started");
        model.addAttribute("sideitems", sis.getAllSideItems());
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
    public String remove(@PathVariable(name = "id") Integer id) {
        System.out.println("begin posting");
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
