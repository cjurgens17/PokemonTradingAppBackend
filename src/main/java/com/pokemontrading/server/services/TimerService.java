package com.pokemontrading.server.services;


import com.pokemontrading.server.models.Timer;
import com.pokemontrading.server.repository.TimerRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class TimerService {

    private final TimerRepository timerRepository;


    public TimerService(TimerRepository timerRepository) {
        this.timerRepository = timerRepository;
    }

    public Timer saveTimer(Timer timer){
       return timerRepository.save(timer);
    }

    public Optional<Timer> getTimerById(Long id){
        return timerRepository.findById(id);
    }

}
