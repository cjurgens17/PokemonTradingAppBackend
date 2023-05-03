package com.poolstore.quickclean.bootstrap;


import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.Timer;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.repository.PokemonRepository;

import com.poolstore.quickclean.repository.TimerRepository;
import com.poolstore.quickclean.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;



@Component
public class DataLoad implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final PokemonRepository pokemonRepository;


    public DataLoad(UserRepository userRepository, PokemonRepository pokemonRepository, TimerRepository timerRepository) {
        this.userRepository = userRepository;
        this.pokemonRepository = pokemonRepository;
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
        john.setFirstName("John");
        john.setLastName("Michaels");
        john.setAdmin(false);
        john.setId(1L);
        john.setEmail("John@example.com");
        john.setPassword("password");
        john.setUsername("john17");
        john.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRT6NaU4Ur8LgGKKc8KqSQoX1KhcMnKgxVYQA&usqp=CAU");
        john.setPokeBalls(500);
        john.setTimer(timer);
        userRepository.save(john);

        Pokemon charizard = new Pokemon();
        charizard.setName("charizard");
        charizard.setUser(john);
        pokemonRepository.save(charizard);

        User mike = new User();
        mike.setFirstName("Mike");
        mike.setLastName("Myers");
        mike.setAdmin(true);
        mike.setId(2L);
        mike.setEmail("Mike@example.com");
        mike.setPassword("password");
        mike.setUsername("mike17");
        mike.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsE7YhmsSgX1GkJCoCzOjbx7n2Je6w7dlwew&usqp=CAU");
        mike.setPokeBalls(4);
        mike.setTimer(timer);
        userRepository.save(mike);
        Pokemon poke = new Pokemon();
        poke.setName("butterfree");
        poke.setUser(mike);
        pokemonRepository.save(poke);
    }
}
