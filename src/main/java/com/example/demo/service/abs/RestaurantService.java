package com.example.demo.service.abs;


import com.example.demo.model.Restaurant;

import java.util.List;

public interface RestaurantService {
    List<Restaurant> getAllRestaurants();
    Restaurant getRestaurantById(int id);
    boolean saveOrUpdateRestaurant(Restaurant restaurant);
    boolean createRestaurant(Restaurant restaurant);
}
