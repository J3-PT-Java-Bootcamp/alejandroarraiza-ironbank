package com.ironhack.ironbankapi.auth.service;

import com.ironhack.ironbankapi.auth.dto.auth.GetTokenRequest;
import com.ironhack.ironbankapi.auth.dto.auth.GetTokenResponse;
import com.ironhack.ironbankapi.core.repository.firebase.FirebaseRepository;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private FirebaseRepository firebaseRepository;

    public AuthService(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }

    public GetTokenResponse getToken(GetTokenRequest request) {
        return new GetTokenResponse(firebaseRepository.getToken(request.getEmail(), request.getPassword()));
    }
}
