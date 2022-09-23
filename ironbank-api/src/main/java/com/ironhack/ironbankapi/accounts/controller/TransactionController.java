package com.ironhack.ironbankapi.accounts.controller;

import com.ironhack.ironbankapi.accounts.dto.TransactionLocalTransferRequestDto;
import com.ironhack.ironbankapi.accounts.dto.TransactionRequestDto;
import com.ironhack.ironbankapi.accounts.dto.TransactionResultDto;
import com.ironhack.ironbankapi.accounts.dto.TransactionThirdPartyTransferRequestDto;
import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
import com.ironhack.ironbankapi.accounts.service.AccountService;
import com.ironhack.ironbankapi.accounts.service.TransactionService;
import com.ironhack.ironbankapi.auth.service.UserService;
import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.transaction.Transaction;
import com.ironhack.ironbankapi.core.model.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    final UserService userService;
    final AccountService accountService;
    final TransactionService transactionService;

    public TransactionController(UserService userService, AccountService accountService,
                                 TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping("/local-transfer")
    TransactionResultDto transfer(Principal principal,
                                  @Valid @RequestBody TransactionLocalTransferRequestDto transactionTransferRequestDto)
            throws IronbankAccountException {
        User user = userService.getUserById(principal.getName());
        Account origin = accountService
                .getAccountByAccountNumber(transactionTransferRequestDto.getOriginAccountNumber());

        if (!origin.checkUserIsOwner(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Account destination = accountService
                .getAccountByAccountNumber(transactionTransferRequestDto.getDestinationAccountNumber());

        return transactionService.localTransfer(origin, destination, transactionTransferRequestDto.getAmount(), user, transactionTransferRequestDto.getSecretKey());
    }

    @PostMapping("/third-party-transfer")
    TransactionResultDto transfer(
            Principal principal,
            @Valid @RequestBody TransactionThirdPartyTransferRequestDto transactionThirdPartyTransferRequestDto)
            throws IronbankAccountException {
        User user = userService.getUserById(principal.getName());
        Account origin = null;
        if (transactionThirdPartyTransferRequestDto.getOriginAccountNumber() != null) {
            origin = accountService
                    .getAccountByAccountNumber(transactionThirdPartyTransferRequestDto.getOriginAccountNumber());

            if (!origin.checkUserIsOwner(user)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

        }

        Account destination = null;
        if (transactionThirdPartyTransferRequestDto.getDestinationAccountNumber() != null) {
            destination = accountService
                    .getAccountByAccountNumber(transactionThirdPartyTransferRequestDto.getDestinationAccountNumber());
        }

        if ((origin == null && destination == null) || (origin != null && destination != null) || transactionThirdPartyTransferRequestDto.getExternalAccountHash() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            return transactionService.thirdPartyTransfer(
                    transactionThirdPartyTransferRequestDto.getExternalAccountHash(),
                    origin, destination, transactionThirdPartyTransferRequestDto.getAmount(),
                    user,
                    transactionThirdPartyTransferRequestDto.getSecretKey());
        }
    }

    @PostMapping("/{accountNumber}/withdraw")
    TransactionResultDto withdraw(Principal principal, @PathVariable String accountNumber,
                                  @Valid @RequestBody TransactionRequestDto transactionRequestDto) throws IronbankAccountException {
        User user = userService.getUserById(principal.getName());

        Account account = accountService.getAccountByAccountNumber(accountNumber);

        if (!account.checkUserIsOwner(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return transactionService.withdraw(account, transactionRequestDto.getAmount(), user, transactionRequestDto.getSecretKey());

    }

    @PostMapping("/{accountNumber}/deposit")
    TransactionResultDto deposit(Principal principal, @PathVariable String accountNumber,
                                 @Valid @RequestBody TransactionRequestDto transactionRequestDto) throws IronbankAccountException {
        User user = userService.getUserById(principal.getName());

        Account account = accountService.getAccountByAccountNumber(accountNumber);

        if (!account.checkUserIsOwner(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return transactionService.deposit(account, transactionRequestDto.getAmount(), user, transactionRequestDto.getSecretKey());
    }

    @PostMapping("/{accountNumber}/update-balance")
    @PreAuthorize("hasAuthority('ADMIN')")
    TransactionResultDto updateBalance(Principal principal, @PathVariable String accountNumber,
                                       @Valid @RequestBody TransactionRequestDto transactionRequestDto) throws IronbankAccountException {
        User user = userService.getUserById(principal.getName());

        Account account = accountService.getAccountByAccountNumber(accountNumber);

        return transactionService.updateBalance(account, transactionRequestDto.getAmount(), user);
    }

}
