package com.poolstore.quickclean.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name="first_name")
    private String firstname;

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

    @Column(name="username")
    private String username;

    @Column(name = "pokemon")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pokemon> userPokemon = new ArrayList<>();





}
