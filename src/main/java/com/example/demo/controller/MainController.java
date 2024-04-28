package com.example.demo.controller;

import com.example.demo.mail.MailConfiguration;
import com.example.demo.model.Order;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.abs.UserService;
import com.example.demo.service.impl.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private UserService acs;

    @Autowired
    private MailSenderService ms;

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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/authenticate")
    public String authenticate(Model model) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        model.addAttribute("code", sb.toString());
        ms.sendNewMail(user.getEmail(),"AUTHENTICATION",sb.toString());
        return "authentication";
    }

    @PostMapping("/authenticate")
    public String authenticate(Model model, @ModelAttribute("code") String code, @ModelAttribute("input") String input) {
        final String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = acs.findUserByEmail(currentUserEmail);
        if (user == null) {
            user = new User();
            user.setName("Stranger");
        }
        model.addAttribute("currentUser", user);
        try {
            if (code.equals(input)) {
                user.setIsActivated(true);
                user.setBonus_coins(0);

                acs.saveOrUpdateUser(user);

                return "request_success";
            } else return "request_fail";
        } catch (Exception e) {
            e.printStackTrace();
            return "request_fail";
        }
    }



    @GetMapping("/logout")
    public String logout() {

        return "login";
    }

}
