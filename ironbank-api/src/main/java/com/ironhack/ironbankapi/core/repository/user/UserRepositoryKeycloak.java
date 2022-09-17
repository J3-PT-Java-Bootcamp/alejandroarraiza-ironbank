package com.ironhack.ironbankapi.core.repository.user;

import com.ironhack.ironbankapi.auth.dto.keycloak.KeycloakUserDto;
import com.ironhack.ironbankapi.auth.exceptions.IronbankAuthException;

public interface UserRepositoryKeycloak {

    String createUserInKeycloak(KeycloakUserDto keycloakUserDto) throws IronbankAuthException;

}