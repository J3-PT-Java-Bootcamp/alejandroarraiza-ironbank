package com.ironhack.ironbankapi.auth.controller;

import com.ironhack.ironbankapi.auth.dto.CreateAccountHolderDto;
import com.ironhack.ironbankapi.auth.dto.CreateAdminDto;
import com.ironhack.ironbankapi.auth.dto.CreateThirdPartyDto;
import com.ironhack.ironbankapi.auth.mapper.UserMapper;
import com.ironhack.ironbankapi.auth.model.UserRole;
import com.ironhack.ironbankapi.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    String createAdmin(@Valid @RequestBody CreateAdminDto createAdminDto) {
        createAdminDto.setUserRole(UserRole.ADMIN);
        return userService.createUser(UserMapper.fromCreateAdminDto(createAdminDto)).toString();
    }

    @PostMapping("/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    String createAccountHolder(@Valid @RequestBody CreateAccountHolderDto createAccountHolderDto) {
        createAccountHolderDto.setUserRole(UserRole.ACCOUNT_HOLDER);
        return userService.createUser(UserMapper.fromCreateAccountHolderDto(createAccountHolderDto)).toString();
    }

    @PostMapping("/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    String createThirdParty(@Valid @RequestBody  CreateThirdPartyDto createThirdPartyDto) {
        createThirdPartyDto.setUserRole(UserRole.THIRD_PARTY);
        return userService.createUser(UserMapper.fromCreateThirdPartyDto(createThirdPartyDto)).toString();
    }

}
