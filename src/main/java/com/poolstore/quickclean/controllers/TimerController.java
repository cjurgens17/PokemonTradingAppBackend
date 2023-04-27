package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.models.Timer;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.TimerService;
import com.poolstore.quickclean.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Timer> updateTimer(@PathVariable Long id , @RequestBody Timer timer){

        Optional<Timer> optTimer = timerService.getTimerById(id);
        if(optTimer.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        Timer timerExists = optTimer.get();

        timerExists.setPrevDate(timer.getPrevDate());

        timerService.saveTimer(timerExists);
        return ResponseEntity.ok(timerExists);
    }

    @GetMapping({"/{id}/getTimer"})
    public ResponseEntity<Timer> getTimer(@PathVariable Long id){
        Optional<Timer> optTimer = timerService.getTimerById(id);

        if(optTimer.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Timer timerExists = optTimer.get();

        return ResponseEntity.ok(timerExists);
    }

    @PostMapping({"/{id}/addPokeBalls"})
    @Transactional
    public ResponseEntity<Integer> addPokeBalls(@PathVariable Long id, @RequestBody Integer pokeBalls){
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
    public ResponseEntity<Integer> deletePokeBalls(@PathVariable Long id, @RequestBody Integer pokeBalls){
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
