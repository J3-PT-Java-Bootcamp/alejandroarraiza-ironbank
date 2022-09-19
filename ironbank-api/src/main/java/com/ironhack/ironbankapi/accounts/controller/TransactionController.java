package com.ironhack.ironbankapi.accounts.controller;

import com.ironhack.ironbankapi.accounts.dto.TransactionRequestDto;
import com.ironhack.ironbankapi.accounts.dto.TransactionResultDto;
import com.ironhack.ironbankapi.accounts.dto.TransactionThirdPartyTransferRequestDto;
import com.ironhack.ironbankapi.accounts.dto.TransactionLocalTransferRequestDto;
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

    @PostMapping("/local-transfer")
    TransactionResultDto transfer(@Valid @RequestBody TransactionLocalTransferRequestDto transactionTransferRequestDto)
            throws IronbankAccountException {
        Account origin = accountService
                .getAccountByAccountNumber(transactionTransferRequestDto.getOriginAccountNumber());
        Account destination = accountService
                .getAccountByAccountNumber(transactionTransferRequestDto.getDestinationAccountNumber());

        return transactionService.localTransfer(origin, destination, transactionTransferRequestDto.getAmount());
    }

    @PostMapping("/third-party-transfer")
    TransactionResultDto transfer(
            @Valid @RequestBody TransactionThirdPartyTransferRequestDto transactionThirdPartyTransferRequestDto)
            throws IronbankAccountException {
        Account origin = null;
        if (transactionThirdPartyTransferRequestDto.getOriginAccountNumber() != null) {
            origin = accountService
                    .getAccountByAccountNumber(transactionThirdPartyTransferRequestDto.getOriginAccountNumber());
        }

        Account destination = null;
        if (transactionThirdPartyTransferRequestDto.getDestinationAccountNumber() != null) {
            destination = accountService
                    .getAccountByAccountNumber(transactionThirdPartyTransferRequestDto.getDestinationAccountNumber());
        }

        if ((origin == null && destination == null) || (origin != null && destination != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            return transactionService.thirdPartyTransfer(
                    transactionThirdPartyTransferRequestDto.getExternalAccountHash(),
                    origin, destination, transactionThirdPartyTransferRequestDto.getAmount());
        }
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
