package com.example.demo.dto;

import com.example.demo.model.*;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Integer id;

    private User user;

    private Restaurant restaurant;

    private Boolean to_deliver;

    private Address address;

    private Timestamp created;

    private Timestamp confirmed;

    private Timestamp assembled;

    private Timestamp delivered;

    private String comment;

    private List<Pizza> pizzas;

    private Set<SideItem> side_items;

    private Feedback feedback;
}
