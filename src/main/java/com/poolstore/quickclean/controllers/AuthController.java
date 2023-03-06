package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.dtos.LoginRequest;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping({"/auth"})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping({"/login"})
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest){
        Optional<User> optional = authService.findByCredentials(loginRequest.getEmail(), loginRequest.getPassword());

        if (!optional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(optional.get());
    }
}
