package com.ironhack.ironbankapi.auth.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.ironhack.ironbankapi.auth.exceptions.IronbankAuthException;
import com.ironhack.ironbankapi.core.model.user.User;
import com.ironhack.ironbankapi.core.repository.firebase.FirebaseRepository;
import com.ironhack.ironbankapi.core.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FirebaseRepository firebaseRepository;

    public UserService(UserRepository userRepository, FirebaseRepository firebaseRepository) {
        this.userRepository = userRepository;
        this.firebaseRepository = firebaseRepository;
    }

    public User createUser(User userToBeCreated) throws IronbankAuthException {
        try {
            userToBeCreated.setId(firebaseRepository.createUser(userToBeCreated.getEmail(), userToBeCreated.getName(),
                    userToBeCreated.getUserRole()));
        } catch (FirebaseAuthException e) {
            System.out.println(e);
        }

        if (userToBeCreated.getId() == null) {
            throw new IronbankAuthException();
        } else {
            userToBeCreated = userRepository.save(userToBeCreated);
        }

        return userToBeCreated;
    }

}
