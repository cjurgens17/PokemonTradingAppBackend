package com.pokemontrading.server.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity {

    @Lob
    @Column(name="text")
    private String text;

    @Column(name="userPokemon")
    private String userPokemon;

    @Column(name="tradePokemon")
    private String tradePokemon;

    @Column(name="username")
    private String username;

    @Column(name="tradePokemonImage")
    private String tradePokemonImage;

    @Column(name="userPokemonImage")
    private String userPokemonImage;

    @Column(name="currentUsername")
    private String currentUsername;

    @Column(name = "isTraded")
    private boolean isTraded;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
