package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.dtos.RegisterRequest;
import com.poolstore.quickclean.exceptions.NotFoundException;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
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

        //make sure to change put to post on the front end

        @PostMapping({"/{id}/addPokemon"})
        public ResponseEntity<User> updatePokemon(@PathVariable Long id , @RequestBody Pokemon pokemon){
            System.out.println("In the addPokemon method in the UserController");
           Optional<User> updateUser = userService.findUserById(id);
           Pokemon newPoke = new Pokemon();
           newPoke.setAbilities(pokemon.getAbilities());
           newPoke.setName(pokemon.getName());
           newPoke.setBackImage(pokemon.getBackImage());
           newPoke.setImage(pokemon.getImage());
           newPoke.setIndex(pokemon.getIndex());

           if(updateUser.isPresent()) {
               User user = updateUser.get();
               List<Pokemon> pokemonList = user.getUserPokemon();
               pokemonList.add(newPoke);
               userService.save(user);

           }else{
               throw new NotFoundException("User does not exist");
           }

            return ResponseEntity.ok().build();
        }
}
