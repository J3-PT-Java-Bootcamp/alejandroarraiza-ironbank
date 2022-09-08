package com.ironhack.ironbankapi.auth.repository;

import com.ironhack.ironbankapi.auth.dto.KeycloakUserDto;

public interface UserRepositoryKeycloak {

    KeycloakUserDto createUserInKeycloak(KeycloakUserDto keycloakUserDto);

}
