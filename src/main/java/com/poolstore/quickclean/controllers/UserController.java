package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.dtos.AddPokemon;
import com.poolstore.quickclean.dtos.RegisterRequest;
import com.poolstore.quickclean.exceptions.NotFoundException;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.UserService;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping({"user"})
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
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

        @PutMapping({"/addPokemon"})
        public ResponseEntity<User> updatePokemon(@RequestBody AddPokemon addPokemon){
            System.out.println("In the addPokemon method in the UserController");
           Optional<User> updateUser = userService.findUserById(addPokemon.getUser().getId());
           Pokemon newPoke = new Pokemon();
           newPoke.setAbilities(addPokemon.getPokemon().getAbilities());
           newPoke.setName(addPokemon.getPokemon().getName());
           newPoke.setBackImage(addPokemon.getPokemon().getBackImage());
           newPoke.setImage(addPokemon.getPokemon().getImage());
           newPoke.setIndex(addPokemon.getPokemon().getIndex());

           if(updateUser.isPresent()) {
               User user = updateUser.get();
               Hibernate.initialize(user.getPokeIndex());
               user.getPokeIndex().add(addPokemon.getPokemon());
           }else{
               throw new NotFoundException("User does not exist");
           }

            return ResponseEntity.ok().build();
        }
}
