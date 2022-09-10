package com.ironhack.ironbankapi.auth.service;

import com.ironhack.ironbankapi.auth.dto.auth.GetTokenRequest;
import com.ironhack.ironbankapi.auth.dto.auth.GetTokenResponse;
import com.ironhack.ironbankapi.auth.repository.auth.AuthRepositoryKeycloak;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepositoryKeycloak authRepository;

    public AuthService(AuthRepositoryKeycloak authRepository) {
        this.authRepository = authRepository;
    }

    public GetTokenResponse getToken(GetTokenRequest request) {
        var token = authRepository.getToken(request.getEmail(), request.getPassword());
        return new GetTokenResponse(token);
    }
}
