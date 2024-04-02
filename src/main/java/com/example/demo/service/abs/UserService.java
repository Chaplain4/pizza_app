package com.example.demo.service.abs;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);

    User save(UserRegistrationDto registrationDto);

    boolean saveOrUpdateUser(User user);
    boolean createUser(User user);
    boolean deleteUser(int id);

    User findUserByEmail(String email);
}
