package com.pokemontrading.server.repository;

import com.pokemontrading.server.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository<Message, Long> {
}
