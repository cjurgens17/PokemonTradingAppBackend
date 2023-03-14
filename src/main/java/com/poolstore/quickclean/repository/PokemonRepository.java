package com.poolstore.quickclean.repository;


import com.poolstore.quickclean.models.Pokemon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PokemonRepository extends CrudRepository<Pokemon, Long> {


}
