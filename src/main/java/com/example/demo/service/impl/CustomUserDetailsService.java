package com.example.demo.service.impl;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.abs.UserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Collection;
import java.util.stream.Collectors;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{


    static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Resource
    private UserRepository userRepository;

    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        logger.info("User : {}", user);
        if(user==null){
            logger.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                true, true, true, true, getGrantedAuthorities(user));
    }


    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for(Role role : user.getRoles()){
            logger.info("UserProfile : {}", role);
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        logger.info("authorities : {}", authorities);
        return authorities;
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Collection <Role> userProfiles) { Collection < ?
            extends GrantedAuthority> mapRoles = userProfiles.stream() .map(profile -> new SimpleGrantedAuthority(profile.getName())) .collect(Collectors.toList());
        return mapRoles; }

}
