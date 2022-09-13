package com.ironhack.ironbankapi.auth.repository.auth;

import com.ironhack.ironbankapi.auth.repository.KeycloakProvider;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
//@PropertySource("classpath:keycloak-admin-client.properties")
public class AuthRepositoryKeycloakImpl implements AuthRepositoryKeycloak{

    private KeycloakProvider keycloakProvider;

    public AuthRepositoryKeycloakImpl(KeycloakProvider keycloakProvider) {
        this.keycloakProvider = keycloakProvider;
    }

    @Override
    public String getToken(String email, String password) {
        // TODO: connect to keycloak api rest and get token for credentials

//         var body = new MultipartBodyBuilder();
//         body.part("grant_type", "client_credentials");
//         body.part("client_id", clientId);
//         body.part("client_secret", clientSecret);
//
//         var response = webClient.post()
//         .uri("/auth/realms/%s/protocol/openid-connect/token".formatted(realm))
//         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//         .body(BodyInserters.fromMultipartData(body.build()))
//         .retrieve()
//         .bodyToMono(String.class)
//         .block();
//
//         System.out.println(response);

        return UUID.randomUUID().toString();
    }
}
