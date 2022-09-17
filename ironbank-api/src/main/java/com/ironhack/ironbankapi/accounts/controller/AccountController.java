package com.ironhack.ironbankapi.accounts.controller;

import com.ironhack.ironbankapi.accounts.dto.AccountsResponseDto;
import com.ironhack.ironbankapi.accounts.dto.CreateCheckingAccountDto;
import com.ironhack.ironbankapi.accounts.dto.CreateCreditAccount;
import com.ironhack.ironbankapi.accounts.dto.CreateSavingsAccountDto;
import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
import com.ironhack.ironbankapi.accounts.service.AccountService;
import com.ironhack.ironbankapi.core.model.account.Account;
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
    CheckingAccount createCheckingAccount(@Valid @RequestBody CreateCheckingAccountDto createCheckingAccountDto) throws IronbankAccountException {
        return this.accountService.createCheckingAccount(createCheckingAccountDto);
    }

    @PostMapping("/savings")
    SavingsAccount createSavingsAccount(@Valid @RequestBody CreateSavingsAccountDto createSavingsAccountDto) throws IronbankAccountException {
        return this.accountService.createSavingsAccount(createSavingsAccountDto);
    }

    @PostMapping("/credit")
    CreditAccount createCreditAccount(@Valid @RequestBody CreateCreditAccount createCreditAccount) throws IronbankAccountException {
        return this.accountService.createCreditAccount(createCreditAccount);
    }

    @GetMapping("/{accountNumber}/balance")
    String getAccountBalance(@PathVariable String accountNumber) throws IronbankAccountException {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        return account.getBalance().getAmount().toString();
    }

    @GetMapping("/all")
    AccountsResponseDto getAllAccounts() {
        return new AccountsResponseDto(
                accountService.getAllCheckingAccounts(),
                accountService.getAllSavingsAccounts(),
                accountService.getAllCreditAccounts()
        );
    }

    @GetMapping("/by-user/{userId}")
    AccountsResponseDto getUserAccounts(@PathVariable String userId) {
        return new AccountsResponseDto(
                accountService.getAllCheckingAccountsByUserId(userId),
                accountService.getAllSavingsAccountsByUserId(userId),
                accountService.getAllCreditAccountsByUserId(userId)
        );
    }

}
