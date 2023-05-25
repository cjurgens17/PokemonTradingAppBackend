package com.pokemontrading.server.repository;

import com.pokemontrading.server.models.Pokemon;
import com.pokemontrading.server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndPassword(String username, String password);
    @Modifying
    @Query("UPDATE Users u SET u.user_pokemon = ?2 WHERE u.id = ?1")
     default void updatePokemonList(Long id, List<Pokemon> pokemon){
    }

    Optional<User> findByUsername(String username);
}
