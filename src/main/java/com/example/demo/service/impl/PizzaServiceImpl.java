package com.example.demo.service.impl;


import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Order;
import com.example.demo.model.Pizza;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PizzaRepository;
import com.example.demo.service.abs.PizzaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class PizzaServiceImpl implements PizzaService {
    @Resource
    private PizzaRepository pr;

    @Resource
    private IngredientRepository ir;

    @Resource
    private OrderRepository or;

    @Override
    public List<PizzaDTO> getAllPizzas() {
        List<PizzaDTO> pizzaDTOS = new ArrayList<>();
        List<Pizza> pizzas = pr.findAll();
        pizzas.stream().forEach(pizza -> {
            PizzaDTO pizzaDTO = mapEntityToDto(pizza);
            pizzaDTOS.add(pizzaDTO);
        });
        return pizzaDTOS;
    }


    @Override
    public Pizza getPizzaById(int id) {
        return pr.getReferenceById(id);
    }

    @Override
    public PizzaDTO saveOrUpdatePizza(Integer pizzaID, PizzaDTO pizzaDTO) {
        Pizza pizza = pr.findById(pizzaID)
                .orElseThrow(() -> new NoSuchElementException("Pizza not found"));

        // Clear existing associations
        for (Ingredient ingredient : pizza.getIngredients()) {
            ingredient.getPizzas().remove(pizza);
            ir.save(ingredient); // Update the ingredient
        }
        pizza.getIngredients().clear();

        for (Order order : pizza.getOrders()) {
            order.getPizzas().remove(pizza);
            or.save(order); // Update the order
        }
        pizza.getOrders().clear();
        pr.save(pizza);

        pizza.setName(pizzaDTO.getName());
        pizza.setPizza_size(pizzaDTO.getPizza_size());
        pizza.setPrice(pizzaDTO.getPrice());
        pizza.setMenu_item(pizzaDTO.getMenu_item());

        Set<Ingredient> updatedIngredients = new HashSet<>();
        for (Ingredient ingredient : pizzaDTO.getIngredients()) {
            Ingredient ingredient1 = ir.findById(ingredient.getId())
                    .orElseThrow(() -> new IllegalStateException("Order not found:" + ingredient.getId()));
            ingredient1.setPizzas(null);
            updatedIngredients.add(ingredient1);
        }
        pizza.setIngredients(updatedIngredients);

        Set<Order> updatedOrders = new HashSet<>();
        for (Order order : pizzaDTO.getOrders()) {
            Order order1 = or.findById(order.getId())
                    .orElseThrow(() -> new IllegalStateException("Order not found:" + order.getId()));
            order1.setPizzas(null);
            updatedOrders.add(order1);
        }
        pizza.setOrders(updatedOrders);

        pr.save(pizza);

        return mapEntityToDTOWithoutMTMFields(pizza);
    }


    @Transactional
    @Override
    public PizzaDTO createPizza(PizzaDTO pizzaDTO) {
        Pizza pizza = new Pizza();
        mapDtoToEntity(pizzaDTO, pizza);
        Pizza savedPizza = pr.save(pizza);
        return mapEntityToDto(savedPizza);
    }

    @Override
    public String deletePizza(Integer pizzaID) {
        Optional<Pizza> optionalPizza = pr.findById(pizzaID);
        if (optionalPizza.isPresent()) {
            Pizza pizza = optionalPizza.get();
            pr.deleteById(pizzaID);
        } else {
            return "fail";
        }

        return "Pizza with id: " + pizzaID + " deleted successfully!";
    }


    @Override
    public List<Pizza> findPizzasByIngredientsId(Integer ingredients_id) {
        return pr.findPizzasByIngredientsId(ingredients_id);
    }

    @Override
    public List<Pizza> findPizzasByOrdersId(Integer orders_id) {
        return pr.findPizzasByOrdersId(orders_id);
    }

    @Override
    public void mapDtoToEntity(PizzaDTO pizzaDTO, Pizza pizza) {
        pizza.setPrice(pizzaDTO.getPrice());
        pizza.setId(pizzaDTO.getId());
        pizza.setMenu_item(pizzaDTO.getMenu_item());
        pizza.setName(pizzaDTO.getName());
        pizza.setPizza_size(pizzaDTO.getPizza_size());
        pizza.setIngredients(pizzaDTO.getIngredients());
        pizza.setOrders(pizzaDTO.getOrders());
    }

    @Override
    public PizzaDTO mapEntityToDto(Pizza pizza) {
        PizzaDTO responseDto = new PizzaDTO();
        List<Ingredient> ingredientSet = ir.findIngredientsByPizzasId(pizza.getId());
        Set<Integer> ids = new HashSet<>();
        ingredientSet.forEach(ingredient -> {
            ids.add(ingredient.getId());
        });
        responseDto.setIngredients(new HashSet<>(ir.findIngredientsByPizzasId(pizza.getId())));
        responseDto.setId(pizza.getId());
        responseDto.setName(pizza.getName());
        responseDto.setMenu_item(pizza.getMenu_item());
        responseDto.setPrice(pizza.getPrice());
        responseDto.setPizza_size(pizza.getPizza_size());
        responseDto.setOrders(new HashSet<>(or.findOrdersByPizzasId(pizza.getId())));
        return responseDto;
    }

    @Override
    public void addIngredientToPizza(Integer pizzaId, Integer ingredientId) {
        pr.addIngredient(pizzaId, ingredientId);
    }

    @Override
    public void clearIngredients(Integer pizzaId) {
        pr.clearIngredients(pizzaId);
    }

    private PizzaDTO mapEntityToDTOWithoutMTMFields(Pizza pizza) {
        PizzaDTO pizzaDTO = new PizzaDTO();
        pizzaDTO.setId(pizza.getId());
        pizzaDTO.setName(pizza.getName());
        pizzaDTO.setPizza_size(pizza.getPizza_size());
        pizzaDTO.setPrice(pizza.getPrice());
        pizzaDTO.setMenu_item(pizza.getMenu_item());
        return pizzaDTO;
    }
}



