package com.ironhack.ironbankapi.auth.repository;

import com.ironhack.ironbankapi.auth.dto.keycloak.KeycloakUserDto;

public interface UserRepositoryKeycloak {

    String createUserInKeycloak(KeycloakUserDto keycloakUserDto);

}
