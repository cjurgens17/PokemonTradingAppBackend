package com.poolstore.quickclean.services;


import com.poolstore.quickclean.models.Timer;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.repository.TimerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TimerService {

    private final TimerRepository timerRepository;


    public TimerService(TimerRepository timerRepository) {
        this.timerRepository = timerRepository;
    }

    public void saveTimer(Timer timer){
        timerRepository.save(timer);
    }

    public Optional<Timer> getTimerById(Long id){
        return timerRepository.findById(id);
    }

//    public Optional<Timer> getUserTimer(User user){
//        List<Timer> timers = new ArrayList<>();
//
//        timerRepository.findAll().forEach(timer -> {
//            if(timer.getUser() == user) {
//                timers.add(timer);
//            }
//        });
//            return Optional.ofNullable(timers.get(0));
//    }

}
