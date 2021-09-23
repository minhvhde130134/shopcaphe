package com.thodung.shopcaphe.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.thodung.shopcaphe.model.User;
import com.thodung.shopcaphe.repository.UserRepository;
import com.thodung.shopcaphe.security.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {            
        return new UserPrincipal(user.get());
        }
        throw new UsernameNotFoundException("Not found: " + email);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return new UserPrincipal(user);
    }
}