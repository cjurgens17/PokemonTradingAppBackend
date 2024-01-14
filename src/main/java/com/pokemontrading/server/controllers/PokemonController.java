package com.pokemontrading.server.controllers;


import com.pokemontrading.server.exceptions.NotFoundException;
import com.pokemontrading.server.models.Pokemon;
import com.pokemontrading.server.models.User;
import com.pokemontrading.server.services.PokemonService;
import com.pokemontrading.server.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping({"pokemon"})
@CrossOrigin(origins = {"https://pokemon-trading-app.vercel.app"}, allowCredentials = "true")
public class PokemonController {

    private final PokemonService pokemonService;
    private final UserService userService;

    public PokemonController(PokemonService pokemonService, UserService userService){
        this.pokemonService = pokemonService;
        this.userService = userService;
    }

    @PostMapping({"addPokemon"})
    public ResponseEntity<Pokemon> addPokemon(@RequestBody Pokemon pokemon){

        if(pokemon == null){
            return ResponseEntity.badRequest().build();
        }

        Pokemon poke = new Pokemon();
        poke.setName(pokemon.getName());
        poke.setWeight(pokemon.getWeight());
        poke.setIndex(pokemon.getIndex());
        poke.setAbilities(pokemon.getAbilities());
        poke.setStatNames(pokemon.getStatNames());
        poke.setBaseStat(pokemon.getBaseStat());
        poke.setImage(pokemon.getImage());
        poke.setBackImage(pokemon.getBackImage());

        Pokemon savedPokemon = pokemonService.savePokemon(poke);
        return ResponseEntity.ok(savedPokemon);
    }

    @PostMapping({"/{id}/addPokemon"})
    public ResponseEntity<Pokemon> updatePokemon(
            @PathVariable Long id,
            @RequestBody Pokemon pokemon
    )
    {
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
        Pokemon savedPokemon = pokemonService.savePokemon(newPoke);

        return ResponseEntity.ok(savedPokemon);
    }
}
