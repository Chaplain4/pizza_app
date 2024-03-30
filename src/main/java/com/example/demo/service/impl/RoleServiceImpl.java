package com.example.demo.service.impl;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.abs.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository rr;

    @Override
    public List<Role> getAllRoles() {
        return rr.findAll();
    }

    @Override
    public Role getRoleById(int id) {
        return rr.getReferenceById(id);
    }

    @Override
    public boolean saveOrUpdateRole(Role role) {
        try {
            rr.save(role);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createRole(Role role) {
        try {
            rr.save(role);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean deleteRole(int id) {
        try {
            rr.deleteById(id);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}
