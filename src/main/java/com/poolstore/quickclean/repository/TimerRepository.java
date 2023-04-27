package com.poolstore.quickclean.repository;

import com.poolstore.quickclean.models.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface TimerRepository extends JpaRepository<Timer, Long> {

}
