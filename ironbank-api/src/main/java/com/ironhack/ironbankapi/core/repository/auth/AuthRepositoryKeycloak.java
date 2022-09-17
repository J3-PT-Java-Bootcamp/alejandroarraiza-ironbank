package com.ironhack.ironbankapi.core.repository.auth;

import org.springframework.stereotype.Repository;

public interface AuthRepositoryKeycloak {

    String getToken(String email, String password);

}
