package com.pokemontrading.server.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "pokemon_users")
public class User extends BaseEntity {

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="pass_word")
    private String password;

    @Column(name="profile_picture")
    private String profilePicture;

    @Column(name="admin")
    private Boolean admin;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="birthdate")
    private Date birthDate;

    @Column(name="username", unique = true)
    private String username;

    @Column(name = "pokeBalls")
    private int pokeBalls;

    @JsonIgnore
    @Column(name = "pokemon")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pokemon> userPokemon = new ArrayList<>();

    @JsonIgnore
    @Column(name="inbox")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> inbox = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private Timer timer;
}
