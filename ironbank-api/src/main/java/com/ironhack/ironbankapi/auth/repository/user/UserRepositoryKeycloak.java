package com.ironhack.ironbankapi.auth.repository.user;

import com.ironhack.ironbankapi.auth.dto.keycloak.KeycloakUserDto;

public interface UserRepositoryKeycloak {

    String createUserInKeycloak(KeycloakUserDto keycloakUserDto);

}
