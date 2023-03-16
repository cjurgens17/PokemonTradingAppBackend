package com.poolstore.quickclean.services;


import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Create custom methods once we get around to testing CRUD and find a need for actions outside of crudrepository.

    public Optional<User> findByCredentials(String email, String password){
        return userRepository.findByEmailAndPassword(email,password);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public Optional<User> findUserById(Long id){
      return userRepository.findById(id);
    }
}
