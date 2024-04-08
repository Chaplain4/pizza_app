package com.example.demo.service.abs;



import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Order;
import com.example.demo.model.Pizza;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    Order getOrderById(int id);
    OrderDTO saveOrUpdateOrder(Integer orderID, OrderDTO orderDTO);
    OrderDTO createOrder(OrderDTO orderDTO);
    String deleteOrder(Integer orderID);
    List<Order> findOrdersByPizzasId(Integer pizzas_id);
}