package com.ironhack.ironbankapi.auth.controller;

import com.ironhack.ironbankapi.auth.dto.create_user.CreateAccountHolderDto;
import com.ironhack.ironbankapi.auth.dto.create_user.CreateAdminDto;
import com.ironhack.ironbankapi.auth.dto.create_user.CreateThirdPartyDto;
import com.ironhack.ironbankapi.auth.exceptions.IronbankAuthException;
import com.ironhack.ironbankapi.auth.mapper.UserMapper;
import com.ironhack.ironbankapi.core.model.user.User;
import com.ironhack.ironbankapi.core.model.user.UserRole;
import com.ironhack.ironbankapi.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    User createAdmin(@Valid @RequestBody CreateAdminDto createAdminDto) {
        createAdminDto.setUserRole(UserRole.ADMIN);
        try {
            return userService.createUser(UserMapper.fromCreateAdminDto(createAdminDto));
        } catch (IronbankAuthException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString(), e);
        }
    }

    @PostMapping("/account-holder")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    User createAccountHolder(@Valid @RequestBody CreateAccountHolderDto createAccountHolderDto) {
        createAccountHolderDto.setUserRole(UserRole.ACCOUNT_HOLDER);
        try {
            return userService.createUser(UserMapper.fromCreateAccountHolderDto(createAccountHolderDto));
        } catch (IronbankAuthException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString(), e);
        }
    }

    @PostMapping("/third-party")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    User createThirdParty(@Valid @RequestBody CreateThirdPartyDto createThirdPartyDto) {
        createThirdPartyDto.setUserRole(UserRole.THIRD_PARTY);
        try {
            return userService.createUser(UserMapper.fromCreateThirdPartyDto(createThirdPartyDto));
        } catch (IronbankAuthException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString(), e);
        }
    }

}
