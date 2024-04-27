package com.example.demo;

import com.example.demo.controller.RestaurantsMVCController;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.*;
import com.example.demo.service.abs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@SpringBootApplication
public class PizzaAppApplication {
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
	@GetMapping("/")
	public String home(Model model) {
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
	public static void main(String[] args) {
		SpringApplication.run(PizzaAppApplication.class, args);
	}

}
