package com.example.demo.service.impl;

import com.example.demo.model.Restaurant;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.service.abs.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantRepository rr;

    @Override
    public List<Restaurant> getAllRestaurants() {
        return rr.findAll();
    }

    @Override
    public Restaurant getRestaurantById(int id) {
        return rr.getReferenceById(id);
    }

    @Override
    public boolean saveOrUpdateRestaurant(Restaurant restaurant) {
        try {
            rr.save(restaurant);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createRestaurant(Restaurant restaurant) {
        try {
            rr.save(restaurant);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}
