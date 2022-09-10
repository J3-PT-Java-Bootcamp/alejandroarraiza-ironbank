package com.ironhack.ironbankapi.auth.controller;

import com.ironhack.ironbankapi.auth.dto.auth.GetTokenRequest;
import com.ironhack.ironbankapi.auth.dto.auth.GetTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    GetTokenResponse getToken(@Valid @RequestBody GetTokenRequest request) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

}
