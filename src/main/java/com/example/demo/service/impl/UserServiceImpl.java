package com.example.demo.service.impl;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.abs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository ar;
    @Autowired
    private RoleRepository rr;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.ar = userRepository;
        this.rr = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return rr.save(role);
    }

    @Override
    public List<User> getAllUsers() {
        return ar.findAll();
    }

    @Override
    public User getUserById(int id) {
        return ar.getReferenceById(id);
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setName(registrationDto.getName());
        user.setPhone(registrationDto.getPhone());
        user.setPassword(passwordEncoder.encode(registrationDto.getPwd()));
        return ar.save(user);
    }

    @Transactional
    @Override
    public boolean saveOrUpdateUser(User user) {
        try {
            ar.save(user);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean createUser(User user) {
        try {
            ar.save(user);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int id) {
        try {
            ar.deleteById(id);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return ar.findByEmail(email);
    }
}
