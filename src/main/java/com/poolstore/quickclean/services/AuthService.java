package com.poolstore.quickclean.services;


import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;


    public AuthService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Optional<User> findByCredentials(String username, String password){
        return userService.findByCredentials(username,password);
    }
}
