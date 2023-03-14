package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.services.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"pokemon"})
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService){
        this.pokemonService = pokemonService;
    }

    @PostMapping({"addPokemon"})
    public ResponseEntity<Pokemon> addPokemon(@RequestBody Pokemon pokemon){

        System.out.println("In add pokemon method for user controller");

        Pokemon poke = new Pokemon();
        poke.setName(pokemon.getName());
        poke.setWeight(pokemon.getWeight());
        poke.setIndex(pokemon.getIndex());

        pokemonService.savePokemon(poke);
        return ResponseEntity.ok().build();
    }
}
