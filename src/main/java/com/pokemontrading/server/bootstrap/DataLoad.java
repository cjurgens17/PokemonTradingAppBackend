package com.pokemontrading.server.bootstrap;


import com.pokemontrading.server.models.Message;
import com.pokemontrading.server.models.Pokemon;
import com.pokemontrading.server.models.Timer;
import com.pokemontrading.server.models.User;
import com.pokemontrading.server.repository.MessageRepository;
import com.pokemontrading.server.repository.PokemonRepository;

import com.pokemontrading.server.repository.TimerRepository;
import com.pokemontrading.server.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class DataLoad implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final PokemonRepository pokemonRepository;
    private final TimerRepository timerRepository;
    private final MessageRepository messageRepository;


    public DataLoad(UserRepository userRepository, PokemonRepository pokemonRepository, TimerRepository timerRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.pokemonRepository = pokemonRepository;
        this.timerRepository = timerRepository;
        this.messageRepository = messageRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        getUsers();
    }
    @Transactional
    private void getUsers(){
        Timer timer = new Timer();
        timer.setId(1L);
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
        timer.setPrevDate(localDateTime);

        User john = new User();
        john.setFirstName("Guest");
        john.setLastName("Doe");
        john.setAdmin(false);
        john.setId(1L);
        john.setEmail("guest@example.com");
        john.setPassword("password");
        john.setUsername("guest");
        john.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTWiPo3r0e_dKyc7bOEEnoPeba3qr192FzglA&usqp=CAU");
        john.setPokeBalls(500);
        userRepository.save(john);

        //Adding message to guest account(john)
        Message message = new Message();
        message.setText("Welcome to our Pokemon Trading App!" +
                " We're thrilled to have you join our community of trainers and collectors." +
                " Here, you can trade, message, and collect your favorite Pokemon with fellow enthusiasts" +
                " from all over the world. Below you will find an example of the trading system. Thank you for joining us and we look forward to seeing" +
                " your collection grow!");
        message.setUserPokemon("Umbreon Example");
        message.setTradePokemon("Vileplume Example");
        message.setUserPokemonImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS--yrrnAfZUHMSYNkzIbskqJNBn7weqJOqoSPsAGQgJAYJFzghSvnBWTmPBIiBjTThCtE&usqp=CAU");
        message.setTradePokemonImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMWK3q3UDNpy3aK7TvWBfbzzgGrEzRh7LiJQ&usqp=CAU");
        message.setTraded(true);
        message.setCurrentUsername("The Pokemon Trading Team");
        message.setUser(john);
        messageRepository.save(message);

        Pokemon charizard = new Pokemon();
        charizard.setName("Charizard");
        charizard.setWeight(905);
        charizard.setIndex(6);
        charizard.setAbilities(List.of("blaze", "solar-power"));
        charizard.setImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/6.png");
        charizard.setBackImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png");

        charizard.setUser(john);
        pokemonRepository.save(charizard);

        Pokemon butterfree = new Pokemon();
        butterfree.setName("Butterfree");
        butterfree.setWeight(320);
        butterfree.setIndex(12);
        butterfree.setAbilities(List.of("compound-eyes", "tinted-lens"));
        butterfree.setImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/12.png");
        butterfree.setBackImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/12.png");

        butterfree.setUser(john);
        pokemonRepository.save(butterfree);

        Pokemon mewtwo = new Pokemon();
        mewtwo.setName("mewtwo");
        mewtwo.setWeight(1220);
        mewtwo.setIndex(150);
        mewtwo.setAbilities(List.of("pressure", "unnerve"));
        mewtwo.setImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/150.png");
        mewtwo.setBackImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/150.png");

        mewtwo.setUser(john);
        pokemonRepository.save(mewtwo);

        timer.setUser(john);
        timerRepository.save(timer);


    }
}
