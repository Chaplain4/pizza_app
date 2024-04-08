package com.example.demo.service.impl;

import com.example.demo.model.SideItem;
import com.example.demo.repository.SideItemRepository;
import com.example.demo.service.abs.SideItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SideItemServiceImpl implements SideItemService {
    @Autowired
    private SideItemRepository sir;

    @Override
    public List<SideItem> getAllSideItems() {
        return sir.findAll();
    }

    @Override
    public SideItem getSideItemById(int id) {
        return sir.getReferenceById(id);
    }

    @Override
    public boolean saveOrUpdateSideItem(SideItem sideItem) {
        try {
            sir.save(sideItem);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createSideItem(SideItem sideItem) {
        try {
            sir.save(sideItem);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean deleteSideItem(int id) {
        try {
            sir.deleteById(id);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public List<SideItem> findSideItemsByOrdersId(Integer orders_id) {
        return sir.findSideItemsByOrdersId(orders_id);
    }
}
