package com.ironhack.ironbankapi.auth.repository.user;

import com.ironhack.ironbankapi.auth.dto.keycloak.KeycloakUserDto;
import com.ironhack.ironbankapi.auth.exceptions.IronbankAuthException;
import com.ironhack.ironbankapi.auth.repository.KeycloakProvider;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryKeycloakImpl implements UserRepositoryKeycloak {

    private KeycloakProvider keycloakProvider;

    public UserRepositoryKeycloakImpl(KeycloakProvider keycloakProvider) {
        this.keycloakProvider = keycloakProvider;
    }

    @Override
    public String createUserInKeycloak(KeycloakUserDto user) throws IronbankAuthException {
        var adminKeycloak = keycloakProvider.getInstance();
        UsersResource usersResource = adminKeycloak.realm(keycloakProvider.getRealm()).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials("Pa55w0rd");

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getUserName());
        kcUser.setLastName(user.getUserName());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        // Change this to change the group logic
         kcUser.setGroups(List.of("members"));

        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            List<UserRepresentation> userList = adminKeycloak.realm(keycloakProvider.getRealm()).users()
                    .search(kcUser.getUsername()).stream().toList();
//                    .filter(userRep -> userRep.getUsername().equals(kcUser.getUsername())).toList();
            var createdUser = userList.get(0);
            return createdUser.getId();
        } else {
            throw new IronbankAuthException();
        }
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
