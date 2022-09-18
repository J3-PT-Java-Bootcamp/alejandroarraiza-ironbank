package com.ironhack.ironbankapi.accounts.controller;

import com.ironhack.ironbankapi.accounts.dto.*;
import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
import com.ironhack.ironbankapi.accounts.service.AccountService;
import com.ironhack.ironbankapi.accounts.service.TransactionService;
import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.account.CheckingAccount;
import com.ironhack.ironbankapi.core.model.account.CreditAccount;
import com.ironhack.ironbankapi.core.model.account.SavingsAccount;
import com.ironhack.ironbankapi.core.model.common.Money;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping("/checking")
    CheckingAccount createCheckingAccount(@Valid @RequestBody CreateCheckingAccountDto createCheckingAccountDto)
            throws IronbankAccountException {
        var account = this.accountService.createCheckingAccount(createCheckingAccountDto);
        transactionService.logAccountCreated(account, createCheckingAccountDto.getBalance().getAmount());
        return account;
    }

    @PostMapping("/savings")
    SavingsAccount createSavingsAccount(@Valid @RequestBody CreateSavingsAccountDto createSavingsAccountDto)
            throws IronbankAccountException {
        var account = this.accountService.createSavingsAccount(createSavingsAccountDto);
        transactionService.logAccountCreated(account, createSavingsAccountDto.getBalance().getAmount());
        return account;
    }

    @PostMapping("/credit")
    CreditAccount createCreditAccount(@Valid @RequestBody CreateCreditAccount createCreditAccount)
            throws IronbankAccountException {
        var account = this.accountService.createCreditAccount(createCreditAccount);
        transactionService.logAccountCreated(account, createCreditAccount.getBalance().getAmount());
        return account;
    }

    @GetMapping("/{accountNumber}")
    Account getAccount(@PathVariable String accountNumber) throws IronbankAccountException {
        return accountService.getAccountByAccountNumber(accountNumber);
    }

    @GetMapping("/{accountNumber}/balance")
    Money getAccountBalance(@PathVariable String accountNumber) throws IronbankAccountException {
        return accountService.getAccountBalance(accountNumber);
    }

    @GetMapping("/all")
    AccountsResponseDto getAllAccounts() {
        return new AccountsResponseDto(
                accountService.getAllCheckingAccounts(),
                accountService.getAllSavingsAccounts(),
                accountService.getAllCreditAccounts());
    }

    @GetMapping("/by-user/{userId}")
    AccountsResponseDto getUserAccounts(@PathVariable String userId) {
        return new AccountsResponseDto(
                accountService.getAllCheckingAccountsByUserId(userId),
                accountService.getAllSavingsAccountsByUserId(userId),
                accountService.getAllCreditAccountsByUserId(userId));
    }

}
