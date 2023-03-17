package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.dtos.RegisterRequest;
import com.poolstore.quickclean.exceptions.NotFoundException;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.PokemonService;
import com.poolstore.quickclean.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
           pokemonService.savePokemon(newPoke);



           if(updateUser.isPresent()) {
               System.out.println("User is present");
               User user = updateUser.get();
               user.getUserPokemon().add(newPoke);
//               userService.updateUserPokemon(id,user.getUserPokemon());
               userService.save(user);

           }else{
               throw new NotFoundException("User does not exist");
           }

            return ResponseEntity.ok().build();
        }
}
