package com.poolstore.quickclean.dtos;


import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPokemon {

     private Pokemon pokemon;
     private User user;
}
