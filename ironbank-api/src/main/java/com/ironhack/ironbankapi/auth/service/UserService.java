package com.ironhack.ironbankapi.auth.service;

import com.ironhack.ironbankapi.auth.mapper.UserMapper;
import com.ironhack.ironbankapi.auth.model.User;
import com.ironhack.ironbankapi.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User userToBeCreated) {
        userRepository.createUserInKeycloak(UserMapper.toKeycloakUserDto(userToBeCreated));
        return userToBeCreated;
    }

}
