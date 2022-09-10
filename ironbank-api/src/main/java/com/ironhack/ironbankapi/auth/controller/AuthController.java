package com.ironhack.ironbankapi.auth.controller;

import com.ironhack.ironbankapi.auth.dto.auth.GetTokenRequest;
import com.ironhack.ironbankapi.auth.dto.auth.GetTokenResponse;
import com.ironhack.ironbankapi.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    GetTokenResponse getToken(@Valid @RequestBody GetTokenRequest request) {
        return authService.getToken(request);
    }

}
