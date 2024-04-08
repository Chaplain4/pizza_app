package com.example.demo.service.impl;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Order;
import com.example.demo.model.Pizza;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PizzaRepository;
import com.example.demo.repository.SideItemRepository;
import com.example.demo.service.abs.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        return null;
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

    private void mapDtoToEntity(OrderDTO orderDTO, Order order) {
        order.setId(orderDTO.getId());
        order.setAddress(orderDTO.getAddress());
        order.setAssembled(orderDTO.getAssembled());
        order.setRestaurant(orderDTO.getRestaurant());
        order.setComment(orderDTO.getComment());
        order.setConfirmed(orderDTO.getConfirmed());
        order.setCreated(orderDTO.getCreated());
        order.setDelivered(orderDTO.getDelivered());
        order.setFeedback(orderDTO.getFeedback());
        order.setPizzas(orderDTO.getPizzas());
        order.setSide_items(orderDTO.getSide_items());
        order.setTo_deliver(orderDTO.getTo_deliver());
        order.setUser(orderDTO.getUser());
    }

    private OrderDTO mapEntityToDto(Order order) {
        OrderDTO responseDTO = new OrderDTO();
        responseDTO.setPizzas(new HashSet<>(pr.findPizzasByOrdersId(order.getId())));
        responseDTO.setSide_items(new HashSet<>(sir.findSideItemsByOrdersId(order.getId())));
        responseDTO.setAddress(or.findById(order.getId()).get().getAddress());
        responseDTO.setAssembled(order.getAssembled());
        responseDTO.setComment(order.getComment());
        responseDTO.setId(order.getId());
        responseDTO.setConfirmed(order.getConfirmed());
        responseDTO.setCreated(order.getCreated());
        responseDTO.setDelivered(order.getDelivered());
        responseDTO.setFeedback(order.getFeedback());
        responseDTO.setTo_deliver(order.getTo_deliver());
        responseDTO.setUser(order.getUser());
        responseDTO.setRestaurant(order.getRestaurant());
        return responseDTO;
    }


    private OrderDTO mapEntityToDTOWithoutMTMFields(Order order) {
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
}
