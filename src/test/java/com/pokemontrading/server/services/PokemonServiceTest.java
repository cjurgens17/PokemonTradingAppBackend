package com.pokemontrading.server.services;


import com.pokemontrading.server.models.Pokemon;
import com.pokemontrading.server.models.User;
import com.pokemontrading.server.repository.PokemonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {

    @InjectMocks
    private PokemonService pokemonService;

    @Mock
    private PokemonRepository pokemonRepository;


    @Test
    void testGetUserPokemon_ReturnsPokemonListForUser() {
        // Arrange
        User existingUser = new User();
        when(pokemonRepository.findAll()).thenReturn(List.of(
                createPokemon("Pikachu", existingUser),
                createPokemon("Charizard", existingUser),
                createPokemon("Bulbasaur", null),
                createPokemon("Squirtle", existingUser)
        ));

        // Act
        List<Pokemon> result = pokemonService.getUserPokemon(existingUser);

        // Assert
        assertEquals(3, result.size());
        verify(pokemonRepository, times(1)).findAll();
        verifyNoMoreInteractions(pokemonRepository);
    }

    // Helper method to create a Pokemon with a given name and user
    private Pokemon createPokemon(String name, User user) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(name);
        pokemon.setUser(user);
        return pokemon;
    }


}
