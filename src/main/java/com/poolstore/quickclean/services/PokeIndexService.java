package com.poolstore.quickclean.services;


import com.poolstore.quickclean.models.PokeIndex;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.repository.PokeIndexRepository;
import org.springframework.stereotype.Service;

@Service
public class PokeIndexService {

    private final PokeIndexRepository pokeIndexRepository;

    public PokeIndexService(PokeIndexRepository pokeIndexRepository) {
        this.pokeIndexRepository = pokeIndexRepository;
    }

    public void savePokeIndex(PokeIndex pokeIndex){
         pokeIndexRepository.save(pokeIndex);
    }
}
