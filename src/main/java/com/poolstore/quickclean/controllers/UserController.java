package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.dtos.LoginRequest;
import com.poolstore.quickclean.dtos.RegisterRequest;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.PokemonService;
import com.poolstore.quickclean.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"user"})
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final PokemonService pokemonService;

    public UserController(UserService userService, PokemonService pokemonService) {
        this.userService = userService;
        this.pokemonService = pokemonService;
    }

        @PostMapping({"/new"})
        public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest){
         System.out.println("In the create User controller method");
         User createUser = new User();

         createUser.setFirstname(registerRequest.getFirstName());
         createUser.setLastName(registerRequest.getLastName());
         createUser.setPhoneNumber(registerRequest.getPhoneNumber());
         createUser.setEmail(registerRequest.getEmail());
         createUser.setBirthDate(registerRequest.getBirthDate());
         createUser.setPassword(registerRequest.getPassword());
         createUser.setUsername(registerRequest.getUsername());

         userService.save(createUser);
         return ResponseEntity.ok().build();
        }

        @PostMapping({"/login"})
        public ResponseEntity<User> userLogin(@RequestBody LoginRequest loginRequest){
            Optional<User> user = userService.findByCredentials(loginRequest.getUsername(), loginRequest.getPassword());
            System.out.println("Username: " + loginRequest.getUsername());
            System.out.println("password: " + loginRequest.getPassword());

            if(user.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(user.get());
        }

        @GetMapping({"/{id}/userPokemon"})
        public ResponseEntity<List<Pokemon>> getUserPokemon(@PathVariable Long id){

        Optional<User> user = userService.findUserById(id);

        if(user.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        User user1 = user.get();

        List<Pokemon> pokemon = pokemonService.getUserPokemon(user1);

        return ResponseEntity.ok(pokemon);
        }
}
