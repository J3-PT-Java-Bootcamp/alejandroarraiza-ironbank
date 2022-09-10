package com.ironhack.ironbankapi.auth.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:keycloak-admin-client.properties")
@NoArgsConstructor
@Getter
public class KeycloakProvider {

    private static Keycloak instance;

    @Value("${keycloakAdmin.serverUrl}")
    String serverUrl;
    @Value("${keycloakAdmin.realm}")
    String realm;
    @Value("${keycloakAdmin.clientId}")
    String clientId;
    @Value("${keycloakAdmin.clientSecret}")
    String clientSecret;

    public Keycloak getInstance() {
        if (instance == null) {
            instance = KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverUrl)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
        }
        return instance;
    }

}
