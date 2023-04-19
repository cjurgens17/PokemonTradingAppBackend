package com.poolstore.quickclean.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name="text")
    private String text;

    @Column(name="userPokemon")
    private String userPokemon;

    @Column(name="tradePokemon")
    private String tradePokemon;

    @Column(name="username")
    private String username;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
