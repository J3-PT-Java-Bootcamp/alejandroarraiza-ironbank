package com.ironhack.ironbankapi.accounts.service;

import com.ironhack.ironbankapi.accounts.dto.CreateCheckingAccountDto;
import com.ironhack.ironbankapi.accounts.dto.CreateCreditAccount;
import com.ironhack.ironbankapi.accounts.dto.CreateSavingsAccountDto;
import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
import com.ironhack.ironbankapi.core.model.account.*;
import com.ironhack.ironbankapi.core.model.user.UserRole;
import com.ironhack.ironbankapi.core.repository.account.AccountNumberRepository;
import com.ironhack.ironbankapi.core.repository.account.CheckingAccountRepository;
import com.ironhack.ironbankapi.core.repository.account.CreditAccountRepository;
import com.ironhack.ironbankapi.core.repository.account.SavingsAccountRepository;
import com.ironhack.ironbankapi.core.repository.user.UserRepository;
import org.iban4j.Iban;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    final UserRepository userRepository;

    final CheckingAccountRepository checkingAccountRepository;

    final CreditAccountRepository creditAccountRepository;

    final SavingsAccountRepository savingsAccountRepository;

    final AccountNumberRepository accountNumberRepository;

    public AccountService(UserRepository userRepository, CheckingAccountRepository checkingAccountRepository, CreditAccountRepository creditAccountRepository, SavingsAccountRepository savingsAccountRepository, AccountNumberRepository accountNumberRepository) {
        this.userRepository = userRepository;
        this.checkingAccountRepository = checkingAccountRepository;
        this.creditAccountRepository = creditAccountRepository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.accountNumberRepository = accountNumberRepository;
    }

    public CheckingAccount createCheckingAccount(CreateCheckingAccountDto createCheckingAccountDto) throws IronbankAccountException {

        if (createCheckingAccountDto.getBalance().getAmount().compareTo(CheckingAccount.MINIMUM_BALANCE.getAmount()) < 0) {
            throw new IronbankAccountException("Balance too low");
        }

        var primaryOwner = userRepository.findById(createCheckingAccountDto.getPrimaryOwnerId()).orElseThrow();

        if (primaryOwner.getUserRole() != UserRole.ACCOUNT_HOLDER) {
            throw new IronbankAccountException("Only Account Holders can open accounts");
        }

        final var primaryAge = Period.between(primaryOwner.getDateOfBirth(), LocalDate.now()).getYears();

        var secondaryOwner = userRepository.findById(createCheckingAccountDto.getSecondaryOwnerId());

        AccountNumber accountNumber = new AccountNumber(Iban.random().toString());
        accountNumberRepository.save(accountNumber);

        var account = new CheckingAccount(
                accountNumber,
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

        if (createCreditAccount.getCreditLimit() != null && createCreditAccount.getCreditLimit().getAmount().compareTo(CreditAccount.MAX_CREDIT_LIMIT.getAmount()) > 0) {
            throw new IronbankAccountException("Unexpected credit limit. Must be less than %s".formatted(CreditAccount.MAX_CREDIT_LIMIT));
        } else if (createCreditAccount.getCreditLimit() == null) {
            createCreditAccount.setCreditLimit(CreditAccount.DEFAULT_CREDIT_LIMIT);
        }

        if (createCreditAccount.getInterestRate() != null && createCreditAccount.getInterestRate() < CreditAccount.MIN_INTEREST_RATE) {
            throw new IronbankAccountException("Unexpected interest rate. Must be greater than %s".formatted(CreditAccount.MIN_INTEREST_RATE));
        } else if (createCreditAccount.getInterestRate() == null) {
            createCreditAccount.setInterestRate(CreditAccount.DEFAULT_INTEREST_RATE);
        }

        if (createCreditAccount.getBalance().getAmount().compareTo(createCreditAccount.getCreditLimit().getAmount()) > 0) {
            throw new IronbankAccountException("Balance over the credit limit");
        }

        var primaryOwner = userRepository.findById(createCreditAccount.getPrimaryOwnerId()).orElseThrow();

        if (primaryOwner.getUserRole() != UserRole.ACCOUNT_HOLDER) {
            throw new IronbankAccountException("Only Account Holders can open accounts");
        }

        var secondaryOwner = userRepository.findById(createCreditAccount.getSecondaryOwnerId());

        AccountNumber accountNumber = new AccountNumber(Iban.random().toString());
        accountNumberRepository.save(accountNumber);

        var account = new CreditAccount(
                accountNumber,
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

    public SavingsAccount createSavingsAccount(CreateSavingsAccountDto createSavingsAccountDto) throws IronbankAccountException {

        if (createSavingsAccountDto.getInterestRate() != null && createSavingsAccountDto.getInterestRate() > SavingsAccount.MAX_INTEREST_RATE) {
            throw new IronbankAccountException("Unexpected interest rate. Must be greater than %s".formatted(SavingsAccount.MAX_INTEREST_RATE));
        } else if (createSavingsAccountDto.getInterestRate() == null) {
            createSavingsAccountDto.setInterestRate(SavingsAccount.DEFAULT_INTEREST_RATE);
        }

        if (createSavingsAccountDto.getMinimumBalance() != null && createSavingsAccountDto.getMinimumBalance().getAmount().compareTo(SavingsAccount.MIN_MINIMUM_BALANCE.getAmount()) < 0) {
            throw new IronbankAccountException("Unexpected minimum balance. Must be greater than %s".formatted(SavingsAccount.MIN_MINIMUM_BALANCE));
        } else if (createSavingsAccountDto.getMinimumBalance() == null) {
            createSavingsAccountDto.setMinimumBalance(SavingsAccount.MIN_MINIMUM_BALANCE);
        }

        if (createSavingsAccountDto.getBalance().getAmount().compareTo(createSavingsAccountDto.getMinimumBalance().getAmount()) < 0) {
            throw new IronbankAccountException("Balance too low");
        }

        var primaryOwner = userRepository.findById(createSavingsAccountDto.getPrimaryOwnerId()).orElseThrow();

        if (primaryOwner.getUserRole() != UserRole.ACCOUNT_HOLDER) {
            throw new IronbankAccountException("Only Account Holders can open accounts");
        }

        var secondaryOwner = userRepository.findById(createSavingsAccountDto.getSecondaryOwnerId());

        AccountNumber accountNumber = new AccountNumber(Iban.random().toString());
        accountNumberRepository.save(accountNumber);

        var account = new SavingsAccount(
                accountNumber,
                createSavingsAccountDto.getBalance(),
                primaryOwner,
                secondaryOwner.orElse(null),
                AccountStatus.ACTIVE,
                createSavingsAccountDto.getSecretKey(),
                createSavingsAccountDto.getMinimumBalance(),
                createSavingsAccountDto.getInterestRate()
        );

        return savingsAccountRepository.save(account);

    }

    public List<CheckingAccount> getAllCheckingAccounts() {
        return checkingAccountRepository.findAll();
    }

    public List<SavingsAccount> getAllSavingsAccounts() {
        return savingsAccountRepository.findAll();
    }

    public List<CreditAccount> getAllCreditAccounts() {
        return creditAccountRepository.findAll();
    }

    public List<CheckingAccount> getAllCheckingAccountsByUserId(String userId) {
        var user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<CheckingAccount> accounts = checkingAccountRepository.findByPrimaryOwner(user.get());
            accounts.addAll(checkingAccountRepository.findBySecondaryOwner(user.get()));
            return accounts;
        } else {
            return new ArrayList<>();
        }
    }

    public List<SavingsAccount> getAllSavingsAccountsByUserId(String userId) {
        var user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<SavingsAccount> accounts = savingsAccountRepository.findByPrimaryOwner(user.get());
            accounts.addAll(savingsAccountRepository.findBySecondaryOwner(user.get()));
            return accounts;
        } else {
            return new ArrayList<>();
        }
    }

    public List<CreditAccount> getAllCreditAccountsByUserId(String userId) {
        var user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<CreditAccount> accounts = creditAccountRepository.findByPrimaryOwner(user.get());
            accounts.addAll(creditAccountRepository.findBySecondaryOwner(user.get()));
            return accounts;
        } else {
            return new ArrayList<>();
        }
    }

    public Account getAccountByAccountNumber(String accountNumber) throws IronbankAccountException {
        var accountNumberFound = accountNumberRepository.findById(accountNumber);
        if(accountNumberFound.isEmpty()) {
            throw new IronbankAccountException("Unexpected account number");
        }

        Account account;
        account = checkingAccountRepository.findByAccountNumber(accountNumberFound.get());
        if(account == null) {
            account = savingsAccountRepository.findByAccountNumber(accountNumberFound.get());
        }
        if(account == null) {
            account = creditAccountRepository.findByAccountNumber(accountNumberFound.get());
        }
        if(account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return account;
    }
}
