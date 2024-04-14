package com.example.demo.service.abs;



import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.model.SideItem;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    Order getOrderById(int id);
    OrderDTO saveOrUpdateOrder(Integer orderID, OrderDTO orderDTO);
    OrderDTO createOrder(OrderDTO orderDTO);
    String deleteOrder(Integer orderID);
    List<Order> findOrdersByPizzasId(Integer pizzas_id);
    List<Order> findOrdersBySide_itemsId(Integer sideItems_id);

    Order mapDtoToEntity(OrderDTO orderDTO, Order order);

    OrderDTO mapEntityToDto(Order order);

    OrderDTO mapEntityToDTOWithoutMTMFields(Order order);

    void addPizzaToOrder(Integer orderId, Integer pizzaId);

    void addSideItemToOrder(Integer orderId, Integer sideItemId);
}