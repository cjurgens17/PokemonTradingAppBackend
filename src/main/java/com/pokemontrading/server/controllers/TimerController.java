package com.pokemontrading.server.controllers;


import com.pokemontrading.server.models.Timer;
import com.pokemontrading.server.models.User;
import com.pokemontrading.server.services.TimerService;
import com.pokemontrading.server.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping({"timer"})
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class TimerController {

    private final TimerService timerService;
    private final UserService userService;

    public TimerController(TimerService timerService, UserService userService) {
        this.timerService = timerService;
        this.userService = userService;
    }

    @PostMapping({"/{id}/updateTimer"})
    @Transactional
    public ResponseEntity<Timer> updateTimer(
            @PathVariable Long id
    )
    {
        //Get User
        Optional<User> optUser = userService.findUserById(id);
        if(optUser.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        User user = optUser.get();

        //Get Users Timer
        Optional<Timer> optTimer = Optional.ofNullable(user.getTimer());
        if(optTimer.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Timer timerExists = optTimer.get();

        //Updating and saving timer
        LocalDateTime localDateTimePlusOne = LocalDateTime.now().plusDays(1);
        timerExists.setPrevDate(localDateTimePlusOne);
        timerService.saveTimer(timerExists);

        //setting user timer and saving
        user.setTimer(timerExists);
        userService.save(user);

        return ResponseEntity.ok(timerExists);
    }

    @GetMapping({"/{id}/getTimer"})
    public ResponseEntity<Timer> getTimer(@PathVariable Long id){
        //getting User
        Optional<User> optUser = userService.findUserById(id);
        if(optUser.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        User user = optUser.get();

        //getting users timer
        Optional<Timer> optTimer = Optional.ofNullable(user.getTimer());
        if(optTimer.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Timer timerExists = optTimer.get();

        return ResponseEntity.ok(timerExists);
    }

    @PostMapping({"/{id}/addPokeBalls"})
    @Transactional
    public ResponseEntity<Integer> addPokeBalls(
            @PathVariable Long id,
            @RequestBody Integer pokeBalls
    )
    {
        Optional<User> optUser = userService.findUserById(id);

        if(optUser.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        User user = optUser.get();

        int currentUserPokeBalls = user.getPokeBalls();
        int newCurrentUserPokeBalls = currentUserPokeBalls + pokeBalls;
        user.setPokeBalls(newCurrentUserPokeBalls);
        userService.save(user);
        return ResponseEntity.ok(newCurrentUserPokeBalls);
    }

    @PostMapping({"/{id}/deletePokeBalls"})
    @Transactional
    public ResponseEntity<Integer> deletePokeBalls(
            @PathVariable Long id,
            @RequestBody Integer pokeBalls
    )
    {
        Optional<User> optUser = userService.findUserById(id);

        if(optUser.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        User user = optUser.get();

        int currentUserPokeBalls = user.getPokeBalls();
        int newCurrentUserPokeBalls = currentUserPokeBalls - pokeBalls;
        user.setPokeBalls(newCurrentUserPokeBalls);
        userService.save(user);
        return ResponseEntity.ok(newCurrentUserPokeBalls);
    }
}
