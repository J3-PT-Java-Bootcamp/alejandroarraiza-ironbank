package com.ironhack.ironbankapi.auth.service;

import com.ironhack.ironbankapi.auth.dto.auth.GetTokenRequest;
import com.ironhack.ironbankapi.auth.dto.auth.GetTokenResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public GetTokenResponse getToken(GetTokenRequest request) {
        return new GetTokenResponse("some-uid");
    }
}
