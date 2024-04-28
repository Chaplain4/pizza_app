package com.example.demo.service.abs;

import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    Role getRoleById(int id);

    boolean saveOrUpdateRole(Role role);

    boolean createRole(Role role);

    boolean deleteRole(int id);
    void addRole(Integer userId, Integer roleId);
    void clearRoles(Integer userId);
}
