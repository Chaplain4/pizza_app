package com.example.demo.repository;

import com.example.demo.model.SideItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SideItemRepository extends JpaRepository<SideItem, Integer> {
    List<SideItem> findSideItemsByOrdersId(Integer orders_id);
}
