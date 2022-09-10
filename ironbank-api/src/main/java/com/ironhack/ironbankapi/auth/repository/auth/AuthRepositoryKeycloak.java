package com.ironhack.ironbankapi.auth.repository.auth;

import org.springframework.stereotype.Repository;

public interface AuthRepositoryKeycloak {

    String getToken(String email, String password);

}
