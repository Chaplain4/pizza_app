package com.example.demo.service.impl;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.abs.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository or;

    @Override
    public List<Order> getAllOrders() {
        return or.findAll();
    }

    @Override
    public Order getOrderById(int id) {
        return or.getReferenceById(id);
    }

    @Override
    public boolean saveOrUpdateOrder(Order order) {
        try {
            or.save(order);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createOrder(Order order) {
        try {
            or.save(order);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}
