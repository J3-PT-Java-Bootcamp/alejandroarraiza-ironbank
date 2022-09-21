package com.ironhack.ironbankapi.accounts.controller;

import com.ironhack.ironbankapi.accounts.dto.*;
import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
import com.ironhack.ironbankapi.accounts.service.AccountService;
import com.ironhack.ironbankapi.accounts.service.TransactionService;
import com.ironhack.ironbankapi.auth.service.UserService;
import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.account.CheckingAccount;
import com.ironhack.ironbankapi.core.model.account.CreditAccount;
import com.ironhack.ironbankapi.core.model.account.SavingsAccount;
import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import com.ironhack.ironbankapi.core.model.user.UserRole;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(UserService userService, AccountService accountService,
            TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping("/checking")
    @PreAuthorize("hasAuthority('ADMIN')")
    CheckingAccount createCheckingAccount(Principal principal,
            @Valid @RequestBody CreateCheckingAccountDto createCheckingAccountDto)
            throws IronbankAccountException {
        User user = userService.getUserById(principal.getName());
        var account = this.accountService.createCheckingAccount(createCheckingAccountDto);
        transactionService.logAccountCreated(account, createCheckingAccountDto.getBalance().getAmount(), user);
        return account;
    }

    @PostMapping("/savings")
    @PreAuthorize("hasAuthority('ADMIN')")
    SavingsAccount createSavingsAccount(Principal principal,
            @Valid @RequestBody CreateSavingsAccountDto createSavingsAccountDto)
            throws IronbankAccountException {
        User user = userService.getUserById(principal.getName());
        var account = this.accountService.createSavingsAccount(createSavingsAccountDto);
        transactionService.logAccountCreated(account, createSavingsAccountDto.getBalance().getAmount(), user);
        return account;
    }

    @PostMapping("/credit")
    @PreAuthorize("hasAuthority('ADMIN')")
    CreditAccount createCreditAccount(Principal principal, @Valid @RequestBody CreateCreditAccount createCreditAccount)
            throws IronbankAccountException {
        User user = userService.getUserById(principal.getName());
        var account = this.accountService.createCreditAccount(createCreditAccount);
        transactionService.logAccountCreated(account, createCreditAccount.getBalance().getAmount(), user);
        return account;
    }

    @GetMapping("/{accountNumber}")
    Account getAccount(Principal principal, @PathVariable String accountNumber) throws IronbankAccountException {
        var user = userService.getUserById(principal.getName());
        var account = accountService.getAccountByAccountNumber(accountNumber);
        if (account.checkUserIsOwner(user)
                || user.getUserRole() == UserRole.ADMIN) {
            return account;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{accountNumber}/balance")
    Money getAccountBalance(Principal principal, @PathVariable String accountNumber) throws IronbankAccountException {
        var user = userService.getUserById(principal.getName());
        var account = accountService.getAccountByAccountNumber(accountNumber);
        if (account.checkUserIsOwner(user)) {
            return new Money(account.getBalance());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<Account>();
        list.addAll(accountService.getAllCheckingAccounts());
        list.addAll(accountService.getAllSavingsAccounts());
        list.addAll(accountService.getAllCreditAccounts());
        return list;
    }

    @GetMapping("/by-user")
    List<Account> getUserAccounts(Principal principal) {
        List<Account> list = new ArrayList<Account>();
        list.addAll(accountService.getAllCheckingAccountsByUserId(principal.getName()));
        list.addAll(accountService.getAllSavingsAccountsByUserId(principal.getName()));
        list.addAll(accountService.getAllCreditAccountsByUserId(principal.getName()));
        return list;
    }

}
