package com.pokemontrading.server.services;


import com.pokemontrading.server.models.Pokemon;
import com.pokemontrading.server.models.User;
import com.pokemontrading.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<User> findByCredentials(String username, String password){
        return userRepository.findByUsernameAndPassword(username,password);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id){
      return userRepository.findById(id);
    }

    public void updateUserPokemon(Long id, List<Pokemon> pokemon){
        userRepository.updatePokemonList(id,pokemon);
    }

    public Optional<User> findByCredentials(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
}
