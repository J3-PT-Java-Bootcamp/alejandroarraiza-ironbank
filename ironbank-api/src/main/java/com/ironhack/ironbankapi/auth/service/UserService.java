package com.ironhack.ironbankapi.auth.service;

import com.ironhack.ironbankapi.auth.exceptions.IronbankAuthException;
import com.ironhack.ironbankapi.auth.mapper.UserMapper;
import com.ironhack.ironbankapi.auth.model.User;
import com.ironhack.ironbankapi.auth.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User userToBeCreated) throws IronbankAuthException {
        userToBeCreated.setId(userRepository.createUserInKeycloak(UserMapper.toKeycloakUserDto(userToBeCreated)));

        if (userToBeCreated.getId() == null) {
            throw new IronbankAuthException();
        } else {
            userToBeCreated = userRepository.save(userToBeCreated);
        }

        return userToBeCreated;
    }

}
