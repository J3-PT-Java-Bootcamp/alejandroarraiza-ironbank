package com.ironhack.ironbankapi.accounts.controller;

import com.ironhack.ironbankapi.accounts.dto.TransactionRequestDto;
import com.ironhack.ironbankapi.accounts.dto.TransactionResultDto;
import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
import com.ironhack.ironbankapi.accounts.service.AccountService;
import com.ironhack.ironbankapi.accounts.service.TransactionService;
import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.transaction.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    final AccountService accountService;
    final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/all")
    List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping("/transfer")
    TransactionResultDto transfer() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/{accountNumber}/withdraw")
    TransactionResultDto withdraw(@PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequestDto transactionRequestDto) throws IronbankAccountException {

        Account account = accountService.getAccountByAccountNumber(accountNumber);

        return transactionService.withdraw(account, transactionRequestDto.getAmount());

    }

    @PostMapping("/{accountNumber}/deposit")
    TransactionResultDto deposit(@PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequestDto transactionRequestDto) throws IronbankAccountException {

        Account account = accountService.getAccountByAccountNumber(accountNumber);

        return transactionService.deposit(account, transactionRequestDto.getAmount());
    }

    @PostMapping("/{accountNumber}/update-balance")
    TransactionResultDto updateBalance(@PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequestDto transactionRequestDto) throws IronbankAccountException {

        Account account = accountService.getAccountByAccountNumber(accountNumber);

        return transactionService.updateBalance(account, transactionRequestDto.getAmount());
    }

}
