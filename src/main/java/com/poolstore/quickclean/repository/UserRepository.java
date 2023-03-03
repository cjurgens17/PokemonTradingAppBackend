package com.poolstore.quickclean.repository;

import com.poolstore.quickclean.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
