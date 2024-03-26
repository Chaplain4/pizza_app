package com.example.demo.service.abs;



import com.example.demo.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(int id);
    boolean saveOrUpdateOrder(Order order);
    boolean createOrder(Order order);
}
