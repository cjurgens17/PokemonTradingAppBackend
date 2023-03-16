package com.poolstore.quickclean.services;


import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.repository.PokemonRepository;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;


    public PokemonService(PokemonRepository pokemonRepository){
        this.pokemonRepository = pokemonRepository;
    }

    public void savePokemon(Pokemon pokemon){
        pokemonRepository.save(pokemon);
    }
}
