package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.models.Timer;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.TimerService;
import com.poolstore.quickclean.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
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
            @PathVariable Long id,
            @RequestParam("date") String stringDate
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
        Date newDate = Date.from(Instant.parse(stringDate));
        timerExists.setPrevDate(newDate);
        System.out.println(timerExists.getPrevDate());
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
