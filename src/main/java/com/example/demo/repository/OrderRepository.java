package com.example.demo.repository;


import com.example.demo.dto.PizzaDTO;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Order;
import com.example.demo.model.Pizza;
import com.example.demo.model.SideItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findOrdersByPizzasId(Integer pizzas_id);

    @Query("SELECT o FROM Order o JOIN o.side_items s WHERE s.id = :sideItemId")
    List<Order> findOrdersBySideItemId(Integer sideItemId);

    @Modifying
    @Query(value = "INSERT INTO orders_pizzas (order_id, pizzza_id) VALUES (:orderId, :pizzaId)", nativeQuery = true)
    void addPizza(@Param("orderId") Integer orderId, @Param("pizzaId") Integer pizzaId);

    @Modifying
    @Query(value = "INSERT INTO orders_side_items (order_id, side_item_id) VALUES (:orderId, :sideItemid)", nativeQuery = true)
    void addSideItem(@Param("orderId") Integer orderId, @Param("sideItemid") Integer sideItemid);
}
