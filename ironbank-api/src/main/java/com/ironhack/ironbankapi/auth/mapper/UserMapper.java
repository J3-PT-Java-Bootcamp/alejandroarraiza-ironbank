package com.ironhack.ironbankapi.auth.mapper;

import com.ironhack.ironbankapi.auth.dto.create_user.CreateAccountHolderDto;
import com.ironhack.ironbankapi.auth.dto.create_user.CreateAdminDto;
import com.ironhack.ironbankapi.auth.dto.create_user.CreateThirdPartyDto;
import com.ironhack.ironbankapi.auth.dto.keycloak.KeycloakUserDto;
import com.ironhack.ironbankapi.auth.model.Address;
import com.ironhack.ironbankapi.auth.model.User;

public abstract class UserMapper {

    public static User fromCreateAdminDto(CreateAdminDto createAdminDto) {
        return User.builder().name(createAdminDto.getName()).email(createAdminDto.getEmail()).userRole(createAdminDto.getUserRole()).build();
    }

    public static User fromCreateAccountHolderDto(CreateAccountHolderDto createAccountHolderDto) {
        var userBuilder = User.builder().name(createAccountHolderDto.getName()).email(createAccountHolderDto.getEmail()).userRole(createAccountHolderDto.getUserRole()).dateOfBirth(createAccountHolderDto.getDateOfBirth()).primaryAddress(Address.builder().city(createAccountHolderDto.getPrimaryCity()).street(createAccountHolderDto.getPrimaryStreet()).postalCode(createAccountHolderDto.getPrimaryPostalCode()).state(createAccountHolderDto.getPrimaryState()).country(createAccountHolderDto.getPrimaryCountry()).build());
        if (createAccountHolderDto.getSecondaryStreet() != null)
            userBuilder.secondaryAddress(Address.builder().city(createAccountHolderDto.getSecondaryCity()).street(createAccountHolderDto.getSecondaryStreet()).postalCode(createAccountHolderDto.getSecondaryPostalCode()).state(createAccountHolderDto.getSecondaryState()).country(createAccountHolderDto.getSecondaryCountry()).build());
        return userBuilder.build();
    }

    public static User fromCreateThirdPartyDto(CreateThirdPartyDto createThirdPartyDto) {
        return User.builder().name(createThirdPartyDto.getName()).email(createThirdPartyDto.getEmail()).userRole(createThirdPartyDto.getUserRole()).externalAccount(createThirdPartyDto.getExternalAccount()).build();
    }

    public static KeycloakUserDto toKeycloakUserDto(User user) {
        return KeycloakUserDto.builder().userName(user.getName()).email(user.getEmail()).password(user.getName()).build();
    }
}
