package com.ironhack.ironbankapi.auth.repository;

import com.ironhack.ironbankapi.auth.dto.KeycloakUserDto;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Repository
@Transactional(readOnly = true)
@PropertySource("classpath:keycloak-admin-client.properties")
public class UserRepositoryKeycloakImpl implements UserRepositoryKeycloak {

    static Keycloak keycloak;

    @Value("${keycloakAdmin.serverUrl}")
    String serverUrl;
    @Value("${keycloakAdmin.realm}")
    String realm;
    @Value("${keycloakAdmin.clientId}")
    String clientId;
    @Value("${keycloakAdmin.clientSecret}")
    String clientSecret;
    @Value("${keycloakAdmin.adminUsername}")
    String userName;
    @Value("${keycloakAdmin.adminPassword}")
    String password;

    public Keycloak getInstance() {
        if (keycloak == null) {

            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                    .build();
        }
        return keycloak;
    }

    @Override
    public KeycloakUserDto createUserInKeycloak(KeycloakUserDto keycloakUserDto) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(keycloakUserDto.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(keycloakUserDto.getUserName());
        user.setFirstName(keycloakUserDto.getUserName());
        user.setCredentials(Collections.singletonList(passwordCredentials));
        user.setEnabled(true);

        UsersResource instance = getInstance().realm(realm).users();
        try (var response = instance.create(user)) {
            System.out.println(response.getEntity());
        } catch (Exception e) {
            System.out.println(e);
        }
        return keycloakUserDto;
    }
}
