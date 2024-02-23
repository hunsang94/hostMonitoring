package com.hostMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hostMonitoring.repository.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        com.hostMonitoring.dto.UserInfo user = userRepository.findById(id).get();

        if (user == null) {
            throw new UsernameNotFoundException("User not found with userId.");
        }

        return User.builder()
                .username(user.getId())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .build();
    }
}
