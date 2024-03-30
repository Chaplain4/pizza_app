package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@SpringBootApplication
public class PizzaAppApplication {
	@GetMapping("/")
	public String home() {
		return "index";
	}
	public static void main(String[] args) {
		SpringApplication.run(PizzaAppApplication.class, args);

	}

}
