package com.pokemontrading.server.controllers;


import com.pokemontrading.server.dtos.LoginRequest;
import com.pokemontrading.server.models.User;
import com.pokemontrading.server.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping({"auth"})
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping({"/login"})
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest){

        Optional<User> optUser = authService.findByCredentials(loginRequest.getUsername(), loginRequest.getPassword());

        if (optUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }else{
            User user = optUser.get();

            return ResponseEntity.ok(user);
        }
    }
}
