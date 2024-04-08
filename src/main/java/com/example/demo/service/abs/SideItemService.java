package com.example.demo.service.abs;


import com.example.demo.model.Pizza;
import com.example.demo.model.SideItem;

import java.util.List;

public interface SideItemService {
    List<SideItem> getAllSideItems();
    SideItem getSideItemById(int id);
    boolean saveOrUpdateSideItem(SideItem sideItem);
    boolean createSideItem(SideItem sideItem);
    boolean deleteSideItem(int id);
    List<SideItem> findSideItemsByOrdersId(Integer orders_id);
}
