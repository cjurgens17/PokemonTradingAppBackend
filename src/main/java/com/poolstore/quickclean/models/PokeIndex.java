package com.poolstore.quickclean.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="poke_index")
public class PokeIndex  extends BaseEntity{


    @OneToMany
    @Column(name="pokemon")
    private List<Pokemon> pokemonList = new ArrayList<>();




}
