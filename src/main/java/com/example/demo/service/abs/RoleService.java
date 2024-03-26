package com.example.demo.service.abs;

import com.example.demo.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(int id);
    boolean saveOrUpdateRole(Role role);
    boolean createRole(Role role);
}
