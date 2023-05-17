package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.exceptions.NotFoundException;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.PokemonService;
import com.poolstore.quickclean.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private PokemonController pokemonController;

    @Mock
    private PokemonService pokemonService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp(){
       mockMvc = MockMvcBuilders.standaloneSetup(pokemonController).build();
    }

    @Test
    void addPokemon_SuccessfulRequest() {
        // Mock input Pokemon
        Pokemon inputPokemon = new Pokemon();
        inputPokemon.setName("Pikachu");
        inputPokemon.setWeight(6);


        // Mock saved Pokemon
        Pokemon savedPokemon = new Pokemon();
        savedPokemon.setId(1L);
        savedPokemon.setName(inputPokemon.getName());
        savedPokemon.setWeight(inputPokemon.getWeight());


        // Mock service method
        when(pokemonService.savePokemon(any(Pokemon.class))).thenReturn(savedPokemon);

        // Perform the request
        ResponseEntity<Pokemon> response = pokemonController.addPokemon(inputPokemon);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedPokemon, response.getBody());

        // Verify service method invocation
        verify(pokemonService, times(1)).savePokemon(any(Pokemon.class));
    }

    @Test
    void addPokemon_BadRequest_NullInput() {
        // Perform the request with null input
        ResponseEntity<Pokemon> response = pokemonController.addPokemon(null);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify service method is not invoked
        verify(pokemonService, never()).savePokemon(any(Pokemon.class));
    }

    @Test
    void updatePokemon_SuccessfulRequest() {
        // Mock input data
        Long userId = 1L;
        Pokemon inputPokemon = new Pokemon();
        inputPokemon.setName("Pikachu");


        // Mock user
        User user = new User();
        user.setId(userId);

        // Mock saved Pokemon
        Pokemon savedPokemon = new Pokemon();
        savedPokemon.setId(1L);
        savedPokemon.setName(inputPokemon.getName());


        // Mock service methods
        when(userService.findUserById(userId)).thenReturn(Optional.of(user));
        when(pokemonService.savePokemon(any(Pokemon.class))).thenReturn(savedPokemon);

        // Perform the request
        ResponseEntity<Pokemon> response = pokemonController.updatePokemon(userId, inputPokemon);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedPokemon, response.getBody());

        // Verify service method invocations
        verify(userService, times(1)).findUserById(userId);
        verify(pokemonService, times(1)).savePokemon(any(Pokemon.class));
    }

    @Test
    void updatePokemon_BadRequest_UserNotFound() {
        // Mock input data
        Long userId = 1L;
        Pokemon inputPokemon = new Pokemon();
        inputPokemon.setName("Pikachu");
        // Set other properties...

        // Mock user not found
        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        // Perform the request and verify the exception
        assertThrows(NotFoundException.class, () -> pokemonController.updatePokemon(userId, inputPokemon));

        // Verify service method invocation
        verify(userService, times(1)).findUserById(userId);
        verify(pokemonService, never()).savePokemon(any(Pokemon.class));
    }

}
