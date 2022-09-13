package com.ironhack.ironbankapi.accounts.controller;

import com.ironhack.ironbankapi.accounts.dto.CreateCheckingAccountDto;
import com.ironhack.ironbankapi.accounts.dto.CreateCreditAccount;
import com.ironhack.ironbankapi.accounts.dto.CreateSavingsAccountDto;
import com.ironhack.ironbankapi.accounts.service.AccountService;
import com.ironhack.ironbankapi.core.model.account.CheckingAccount;
import com.ironhack.ironbankapi.core.model.account.CreditAccount;
import com.ironhack.ironbankapi.core.model.account.SavingsAccount;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/checking")
    CheckingAccount createCheckingAccount(@Valid @RequestBody CreateCheckingAccountDto createCheckingAccountDto) {
        return this.accountService.createCheckingAccount(createCheckingAccountDto);
    }

    @PostMapping("/savings")
    SavingsAccount createSavingsAccount(@Valid @RequestBody CreateSavingsAccountDto createSavingsAccountDto) {
        return this.accountService.createSavingsAccount(createSavingsAccountDto);
    }

    @PostMapping("/credit")
    CreditAccount createCreditAccount(@Valid @RequestBody CreateCreditAccount createCreditAccount) {
        return this.accountService.createCreditAccount(createCreditAccount);
    }

    @GetMapping("/{accountNumber}/balance")
    void getAccountBalance(@PathVariable String accountNumber) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/all")
    void getAllAccounts() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/by-user/{userId}")
    void getUserAccounts(@PathVariable String userId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

}
