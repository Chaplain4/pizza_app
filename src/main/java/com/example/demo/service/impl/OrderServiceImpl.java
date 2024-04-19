package com.example.demo.service.impl;

import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.model.Pizza;
import com.example.demo.model.SideItem;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PizzaRepository;
import com.example.demo.repository.SideItemRepository;
import com.example.demo.service.abs.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository or;
    @Autowired
    private PizzaRepository pr;
    @Autowired
    private SideItemRepository sir;



    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> orderDTOs = new ArrayList<>();
        List<Order> orders = or.findAll();
        orders.stream().forEach(order -> {
            OrderDTO orderDTO = mapEntityToDto(order);
            orderDTOs.add(orderDTO);
        });
        return orderDTOs;
    }

    @Override
    public Order getOrderById(int id) {
        return or.getReferenceById(id);
    }

    @Override
    public OrderDTO saveOrUpdateOrder(Integer orderID, OrderDTO orderDTO) {
        Order order = or.findById(orderID)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        // Clear existing associations
        for (Pizza pizza : order.getPizzas()) {
            pizza.getOrders().remove(order);
            pr.save(pizza); // Update the ingredient
        }
        order.getPizzas().clear();

        for (SideItem sideItem : order.getSide_items()) {
            sideItem.getOrders().remove(order);
            sir.save(sideItem); // Update the order
        }
        order.getSide_items().clear();
        or.save(order);



        Set<Pizza> updatedPizzas = new HashSet<>();
        for (Pizza pizza : orderDTO.getPizzas()) {
            Pizza pizza1 = pr.findById(pizza.getId())
                    .orElseThrow(() -> new IllegalStateException("Pizza not found:" + pizza.getId()));
            pizza1.setOrders(null);
            updatedPizzas.add(pizza1);
        }
        order.setPizzas(updatedPizzas);

        Set<SideItem> updatedSideItems = new HashSet<>();
        for (SideItem sideItem : orderDTO.getSide_items()) {
            SideItem sideItem1 = sir.findById(sideItem.getId())
                    .orElseThrow(() -> new IllegalStateException("SideItem not found:" + sideItem.getId()));
            sideItem1.setOrders(null);
            updatedSideItems.add(sideItem1);
        }
        order.setSide_items(updatedSideItems);

        or.save(order);

        return mapEntityToDTOWithoutMTMFields(order);
    }

    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        mapDtoToEntity(orderDTO, order);
        Order savedOrder = or.save(order);
        return mapEntityToDto(savedOrder);
    }

    @Override
    public String deleteOrder(Integer orderID) {
        Optional<Order> optionalOrder = or.findById(orderID);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            or.deleteById(orderID);
        } else {
            return "fail";
        }
        return "Order with id: " + orderID + " deleted successfully!";
    }

    @Override
    public List<Order> findOrdersByPizzasId(Integer pizzas_id) {
        return or.findOrdersByPizzasId(pizzas_id);
    }

    @Override
    public List<Order> findOrdersBySide_itemsId(Integer sideItems_id) {
        return or.findOrdersBySideItemId(sideItems_id);
    }

    @Override
    public Order mapDtoToEntity(OrderDTO orderDTO, Order order) {
        order.setId(orderDTO.getId());
        order.setAddress(orderDTO.getAddress());
        order.setAssembled(orderDTO.getAssembled());
        order.setRestaurant(orderDTO.getRestaurant());
        order.setComment(orderDTO.getComment());
        order.setConfirmed(orderDTO.getConfirmed());
        order.setCreated(orderDTO.getCreated());
        order.setDelivered(orderDTO.getDelivered());
        order.setFeedback(orderDTO.getFeedback());
        order.setPizzas(new HashSet<>(orderDTO.getPizzas()));
        order.setSide_items(orderDTO.getSide_items());
        order.setTo_deliver(orderDTO.getTo_deliver());
        order.setUser(orderDTO.getUser());
        return order;
    }

    @Override
    public OrderDTO mapEntityToDto(Order order) {
        OrderDTO responseDTO = new OrderDTO();
        responseDTO.setPizzas(new ArrayList<>(order.getPizzas()));
        responseDTO.setSide_items(new HashSet<>(order.getSide_items()));
        if (order.getAddress() != null) {
            responseDTO.setAddress(order.getAddress());
        }
        responseDTO.setAssembled(order.getAssembled());
        responseDTO.setComment(order.getComment());
        responseDTO.setId(order.getId());
        responseDTO.setConfirmed(order.getConfirmed());
        responseDTO.setCreated(order.getCreated());
        responseDTO.setDelivered(order.getDelivered());
        responseDTO.setFeedback(order.getFeedback());
        responseDTO.setTo_deliver(order.getTo_deliver());
        if (order.getUser() != null) {
            responseDTO.setUser(order.getUser());
        }
        if (order.getRestaurant() !=null) {
            responseDTO.setRestaurant(order.getRestaurant());
        }
        return responseDTO;
    }

    @Override
    public OrderDTO mapEntityToDTOWithoutMTMFields(Order order) {
        OrderDTO responseDTO = new OrderDTO();
        responseDTO.setAddress(order.getAddress());
        responseDTO.setAssembled(order.getAssembled());
        responseDTO.setComment(order.getComment());
        responseDTO.setId(order.getId());
        responseDTO.setConfirmed(order.getConfirmed());
        responseDTO.setCreated(order.getCreated());
        responseDTO.setDelivered(order.getDelivered());
        responseDTO.setFeedback(order.getFeedback());
        responseDTO.setTo_deliver(order.getTo_deliver());
        return responseDTO;
    }

    @Override
    public void addPizzaToOrder(Integer orderId, Integer pizzaId) {
        or.addPizza(orderId,pizzaId);
    }

    @Override
    public void addSideItemToOrder(Integer orderId, Integer sideItemId) {
        or.addSideItem(orderId,sideItemId);
    }
}
