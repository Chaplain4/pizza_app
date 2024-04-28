package com.example.demo.repository;

import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Modifying
    @Query(value = "INSERT IGNORE INTO users_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    void addRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    @Modifying
    @Query(value = "DELETE FROM users_roles WHERE  (user_id = :userId)", nativeQuery = true)
    void clearRoles(@Param("userId") Integer userId);
}
