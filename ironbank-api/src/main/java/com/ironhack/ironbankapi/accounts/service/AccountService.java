package com.ironhack.ironbankapi.accounts.service;

import com.ironhack.ironbankapi.accounts.dto.CreateCheckingAccountDto;
import com.ironhack.ironbankapi.accounts.dto.CreateCreditAccount;
import com.ironhack.ironbankapi.accounts.dto.CreateSavingsAccountDto;
import com.ironhack.ironbankapi.core.model.account.CheckingAccount;
import com.ironhack.ironbankapi.core.model.account.CreditAccount;
import com.ironhack.ironbankapi.core.model.account.SavingsAccount;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService {
    public CheckingAccount createCheckingAccount(CreateCheckingAccountDto createCheckingAccountDto) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    public SavingsAccount createSavingsAccount(CreateSavingsAccountDto createSavingsAccountDto) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    public CreditAccount createCreditAccount(CreateCreditAccount createCreditAccount) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
