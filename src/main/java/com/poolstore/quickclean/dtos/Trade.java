package com.poolstore.quickclean.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Trade {


    private String username;
    private String currentUsername;
    private String userPokemon;
    private String tradePokemon;

}
