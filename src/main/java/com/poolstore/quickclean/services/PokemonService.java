package com.poolstore.quickclean.services;


import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.repository.PokemonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;


    public PokemonService(PokemonRepository pokemonRepository){
        this.pokemonRepository = pokemonRepository;
    }

    public Pokemon savePokemon(Pokemon pokemon){
       return pokemonRepository.save(pokemon);
    }

    public List<Pokemon> getUserPokemon(User user){

        List<Pokemon> pokemon = new ArrayList<>();

        pokemonRepository.findAll().forEach(pokemon1 -> {
            if(pokemon1.getUser() == null){
                User nullUser = new User();
                pokemon1.setUser(nullUser);
            }
            if(pokemon1.getUser().equals(user)){
                pokemon.add(pokemon1);
            }
        });

        return pokemon;
    }

    public void deletePokemon(Pokemon pokemon){
        pokemonRepository.delete(pokemon);
    }
}
