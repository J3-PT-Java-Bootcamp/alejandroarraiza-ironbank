package com.ironhack.ironbankapi.auth.repository.user;

import com.ironhack.ironbankapi.auth.dto.keycloak.KeycloakUserDto;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
@Transactional(readOnly = true)
@PropertySource("classpath:keycloak-admin-client.properties")
public class UserRepositoryKeycloakImpl implements UserRepositoryKeycloak {

    @Value("${keycloakAdmin.serverUrl}")
    String serverUrl;
    @Value("${keycloakAdmin.realm}")
    String realm;
    @Value("${keycloakAdmin.clientId}")
    String clientId;
    @Value("${keycloakAdmin.clientSecret}")
    String clientSecret;

    private final WebClient webClient;

    public UserRepositoryKeycloakImpl(WebClient.Builder webClientBuilder,
            @Value("${keycloakAdmin.serverUrl}") String serverUrl) {
        this.webClient = webClientBuilder.baseUrl(serverUrl).build();
    }

    @Override
    public String createUserInKeycloak(KeycloakUserDto keycloakUserDto) {
        // TODO: connect to keycloak api rest and create user

        // var body = new MultipartBodyBuilder();
        // body.part("grant_type", "client_credentials");
        // body.part("client_id", clientId);
        // body.part("client_secret", clientSecret);

        // var response = webClient.post()
        // .uri("/auth/realms/%s/protocol/openid-connect/token".formatted(realm))
        // .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        // .body(BodyInserters.fromMultipartData(body.build()))
        // .retrieve()
        // .bodyToMono(String.class)
        // .block();

        // System.out.println(response);

        return UUID.randomUUID().toString();

    }
}
