package com.ironhack.ironbankapi.accounts.service;

import com.ironhack.ironbankapi.accounts.dto.CreateCheckingAccountDto;
import com.ironhack.ironbankapi.accounts.dto.CreateCreditAccount;
import com.ironhack.ironbankapi.accounts.dto.CreateSavingsAccountDto;
import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
import com.ironhack.ironbankapi.core.model.account.*;
import com.ironhack.ironbankapi.core.model.user.UserRole;
import com.ironhack.ironbankapi.core.repository.account.CheckingAccountRepository;
import com.ironhack.ironbankapi.core.repository.account.CreditAccountRepository;
import com.ironhack.ironbankapi.core.repository.user.UserRepository;
import org.iban4j.Iban;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AccountService {

    final UserRepository userRepository;

    final CheckingAccountRepository checkingAccountRepository;

    final CreditAccountRepository creditAccountRepository;

    public AccountService(UserRepository userRepository, CheckingAccountRepository checkingAccountRepository, CreditAccountRepository creditAccountRepository) {
        this.userRepository = userRepository;
        this.checkingAccountRepository = checkingAccountRepository;
        this.creditAccountRepository = creditAccountRepository;
    }

    public CheckingAccount createCheckingAccount(CreateCheckingAccountDto createCheckingAccountDto) throws IronbankAccountException {

        if(createCheckingAccountDto.getBalance().getAmount().compareTo(CheckingAccount.MINIMUM_BALANCE.getAmount()) < 0) {
            throw new IronbankAccountException("Balance too low");
        }

        var primaryOwner = userRepository.findById(createCheckingAccountDto.getPrimaryOwnerId()).orElseThrow();

        if(primaryOwner.getUserRole() != UserRole.ACCOUNT_HOLDER) {
            throw new IronbankAccountException("Only Account Holders can open accounts");
        }

        final var primaryAge = Period.between(primaryOwner.getDateOfBirth(), LocalDate.now()).getYears();

        var secondaryOwner = userRepository.findById(createCheckingAccountDto.getSecondaryOwnerId());

        var account = new CheckingAccount(
                Iban.random().toFormattedString(),
                createCheckingAccountDto.getBalance(),
                primaryOwner,
                secondaryOwner.orElse(null),
                AccountStatus.ACTIVE,
                createCheckingAccountDto.getSecretKey(),
                primaryAge < CheckingAccount.STUDENT_AGE_LIMIT ? CheckingAccountType.STUDENT : CheckingAccountType.DEFAULT
        );

        return checkingAccountRepository.save(account);

    }

    public CreditAccount createCreditAccount(CreateCreditAccount createCreditAccount) throws IronbankAccountException {

        if(createCreditAccount.getCreditLimit() != null &&  createCreditAccount.getCreditLimit().getAmount().compareTo(CreditAccount.MAX_CREDIT_LIMIT.getAmount()) > 0) {
            throw new IronbankAccountException("Unexpected credit limit. Must be less than %s".formatted(CreditAccount.MAX_CREDIT_LIMIT.toString()));
        } else if(createCreditAccount.getCreditLimit() == null ) {
            createCreditAccount.setCreditLimit(CreditAccount.DEFAULT_CREDIT_LIMIT);
        }

        if(createCreditAccount.getInterestRate() != null &&  createCreditAccount.getInterestRate() < CreditAccount.MIN_INTEREST_RATE) {
            throw new IronbankAccountException("Unexpected interest rate. Must be greater than %s".formatted(CreditAccount.MIN_INTEREST_RATE));
        } else if(createCreditAccount.getInterestRate() == null ) {
            createCreditAccount.setInterestRate(CreditAccount.DEFAULT_INTEREST_RATE);
        }

        if(createCreditAccount.getBalance().getAmount().compareTo(createCreditAccount.getCreditLimit().getAmount()) > 0) {
            throw new IronbankAccountException("Balance over the credit limit");
        }

        var primaryOwner = userRepository.findById(createCreditAccount.getPrimaryOwnerId()).orElseThrow();

        if(primaryOwner.getUserRole() != UserRole.ACCOUNT_HOLDER) {
            throw new IronbankAccountException("Only Account Holders can open accounts");
        }

        var secondaryOwner = userRepository.findById(createCreditAccount.getSecondaryOwnerId());

        var account = new CreditAccount(
                Iban.random().toFormattedString(),
                createCreditAccount.getBalance(),
                primaryOwner,
                secondaryOwner.orElse(null),
                AccountStatus.ACTIVE,
                createCreditAccount.getSecretKey(),
                createCreditAccount.getCreditLimit(),
                createCreditAccount.getInterestRate()
        );

        return creditAccountRepository.save(account);

    }

    public SavingsAccount createSavingsAccount(CreateSavingsAccountDto createSavingsAccountDto) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
