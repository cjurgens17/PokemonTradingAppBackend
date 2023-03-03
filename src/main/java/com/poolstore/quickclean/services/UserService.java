package com.poolstore.quickclean.services;


import com.poolstore.quickclean.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Create custom methods once we get around to testing CRUD and find a need for actions outside of crudrepository.
}
