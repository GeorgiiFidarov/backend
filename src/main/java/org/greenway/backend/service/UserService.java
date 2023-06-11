package org.greenway.backend.service;

import org.greenway.backend.model.User;
import org.greenway.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElse(null);
    }
}
