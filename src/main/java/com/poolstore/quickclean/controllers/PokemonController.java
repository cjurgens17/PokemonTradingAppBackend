package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.exceptions.NotFoundException;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.PokemonService;
import com.poolstore.quickclean.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping({"pokemon"})
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class PokemonController {

    private final PokemonService pokemonService;
    private final UserService userService;

    public PokemonController(PokemonService pokemonService, UserService userService){
        this.pokemonService = pokemonService;
        this.userService = userService;
    }

    @PostMapping({"addPokemon"})
    public ResponseEntity<Pokemon> addPokemon(@RequestBody Pokemon pokemon){

        System.out.println("In add pokemon method for user controller");

        Pokemon poke = new Pokemon();
        poke.setName(pokemon.getName());
        poke.setWeight(pokemon.getWeight());
        poke.setIndex(pokemon.getIndex());
        poke.setAbilities(pokemon.getAbilities());
        poke.setStatNames(pokemon.getStatNames());
        poke.setBaseStat(pokemon.getBaseStat());
        poke.setImage(pokemon.getImage());
        poke.setBackImage(pokemon.getBackImage());

        pokemonService.savePokemon(poke);
        return ResponseEntity.ok().build();
    }

    @PostMapping({"/{id}/addPokemon"})
    public ResponseEntity<User> updatePokemon(@PathVariable Long id, @RequestBody Pokemon pokemon) {
        System.out.println("In the addPokemon method in the UserController");
        Optional<User> updateUser = userService.findUserById(id);
        User user;
        if (updateUser.isPresent()) {
            user = updateUser.get();
        } else {
            throw new NotFoundException("User does not exist");
        }
        
        Pokemon newPoke = new Pokemon();
        newPoke.setAbilities(pokemon.getAbilities());
        newPoke.setName(pokemon.getName());
        newPoke.setBackImage(pokemon.getBackImage());
        newPoke.setImage(pokemon.getImage());
        newPoke.setIndex(pokemon.getIndex());
        newPoke.setUser(user);
        newPoke.setWeight(pokemon.getWeight());
        System.out.println("pokemon: " + newPoke);
        pokemonService.savePokemon(newPoke);

        return ResponseEntity.ok().build();
    }
}
