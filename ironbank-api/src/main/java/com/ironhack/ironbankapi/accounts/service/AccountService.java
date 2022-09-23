package com.ironhack.ironbankapi.accounts.service;

import com.ironhack.ironbankapi.accounts.dto.CreateCheckingAccountDto;
import com.ironhack.ironbankapi.accounts.dto.CreateCreditAccount;
import com.ironhack.ironbankapi.accounts.dto.CreateSavingsAccountDto;
import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
import com.ironhack.ironbankapi.core.model.account.*;
import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
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

import java.math.BigDecimal;
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

    final TransactionService transactionService;


    public AccountService(UserRepository userRepository, CheckingAccountRepository checkingAccountRepository,
                          CreditAccountRepository creditAccountRepository, SavingsAccountRepository savingsAccountRepository,
                          AccountNumberRepository accountNumberRepository, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.checkingAccountRepository = checkingAccountRepository;
        this.creditAccountRepository = creditAccountRepository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.accountNumberRepository = accountNumberRepository;
        this.transactionService = transactionService;
    }


    public CheckingAccount createCheckingAccount(User user, CreateCheckingAccountDto createCheckingAccountDto)
            throws IronbankAccountException {

        if (createCheckingAccountDto.getBalance().getAmount()
                .compareTo(CheckingAccount.MINIMUM_BALANCE.getAmount()) < 0) {
            throw new IronbankAccountException("Balance too low");
        }

        var primaryOwner = userRepository.findById(createCheckingAccountDto.getPrimaryOwnerId()).orElseThrow();

        if (primaryOwner.getUserRole() != UserRole.ACCOUNT_HOLDER) {
            throw new IronbankAccountException("Only Account Holders can open accounts");
        }

        User secondaryOwner = null;

        if(createCheckingAccountDto.getSecondaryOwnerId() != null) {
            var secondaryOwnerOpt = userRepository.findById(createCheckingAccountDto.getSecondaryOwnerId());

            if (secondaryOwnerOpt.isPresent() && secondaryOwnerOpt.get().getUserRole() != UserRole.ACCOUNT_HOLDER) {
                throw new IronbankAccountException("Only Account Holders can open accounts");
            } else if(secondaryOwnerOpt.isPresent()) {
                secondaryOwner = secondaryOwnerOpt.get();
            }
        }

        final var primaryAge = Period.between(primaryOwner.getDateOfBirth(), LocalDate.now()).getYears();

        AccountNumber accountNumber = new AccountNumber(Iban.random().toString());
        accountNumberRepository.save(accountNumber);

        var account = new CheckingAccount(
                accountNumber,
                primaryOwner,
                secondaryOwner,
                AccountStatus.ACTIVE,
                createCheckingAccountDto.getSecretKey(),
                primaryAge < CheckingAccount.STUDENT_AGE_LIMIT ? CheckingAccountType.STUDENT
                        : CheckingAccountType.DEFAULT,
                primaryAge < CheckingAccount.STUDENT_AGE_LIMIT ? new Money(new BigDecimal(0))
                        : CheckingAccount.MINIMUM_BALANCE,
                primaryAge < CheckingAccount.STUDENT_AGE_LIMIT ? new Money(new BigDecimal(0))
                        : CheckingAccount.MONTHLY_MAINTENANCE_FEE,
                primaryAge < CheckingAccount.STUDENT_AGE_LIMIT ? AccountType.STUDENT_CHECKING : AccountType.CHECKING);

        account = checkingAccountRepository.save(account);

        transactionService.logAccountCreated(account, createCheckingAccountDto.getBalance().getAmount(), user);

        return (CheckingAccount) getAccountByAccountNumber(account.getAccountNumber().getIban());

    }


    public CreditAccount createCreditAccount(User user, CreateCreditAccount createCreditAccount) throws IronbankAccountException {

        if (createCreditAccount.getCreditLimit() != null && createCreditAccount.getCreditLimit().getAmount()
                .compareTo(CreditAccount.MAX_CREDIT_LIMIT.getAmount()) > 0) {
            throw new IronbankAccountException(
                    "Unexpected credit limit. Must be less than %s".formatted(CreditAccount.MAX_CREDIT_LIMIT));
        } else if (createCreditAccount.getCreditLimit() == null) {
            createCreditAccount.setCreditLimit(CreditAccount.DEFAULT_CREDIT_LIMIT);
        }

        if (createCreditAccount.getInterestRate() != null
                && createCreditAccount.getInterestRate() < CreditAccount.MIN_INTEREST_RATE) {
            throw new IronbankAccountException(
                    "Unexpected interest rate. Must be greater than %s".formatted(CreditAccount.MIN_INTEREST_RATE));
        } else if (createCreditAccount.getInterestRate() == null) {
            createCreditAccount.setInterestRate(CreditAccount.DEFAULT_INTEREST_RATE);
        }

        if (createCreditAccount.getBalance().getAmount()
                .compareTo(createCreditAccount.getCreditLimit().getAmount()) > 0) {
            throw new IronbankAccountException("Balance over the credit limit");
        }

        var primaryOwner = userRepository.findById(createCreditAccount.getPrimaryOwnerId()).orElseThrow();

        if (primaryOwner.getUserRole() != UserRole.ACCOUNT_HOLDER) {
            throw new IronbankAccountException("Only Account Holders can open accounts");
        }

        User secondaryOwner = null;

        if(createCreditAccount.getSecondaryOwnerId() != null) {
            var secondaryOwnerOpt = userRepository.findById(createCreditAccount.getSecondaryOwnerId());

            if (secondaryOwnerOpt.isPresent() && secondaryOwnerOpt.get().getUserRole() != UserRole.ACCOUNT_HOLDER) {
                throw new IronbankAccountException("Only Account Holders can open accounts");
            } else if(secondaryOwnerOpt.isPresent()) {
                secondaryOwner = secondaryOwnerOpt.get();
            }
        }

        AccountNumber accountNumber = new AccountNumber(Iban.random().toString());
        accountNumberRepository.save(accountNumber);

        var account = new CreditAccount(
                accountNumber,
                primaryOwner,
                secondaryOwner,
                AccountStatus.ACTIVE,
                createCreditAccount.getSecretKey(),
                createCreditAccount.getCreditLimit(),
                createCreditAccount.getInterestRate());

        account = creditAccountRepository.save(account);

        transactionService.logAccountCreated(account, createCreditAccount.getBalance().getAmount(), user);

        return (CreditAccount) getAccountByAccountNumber(account.getAccountNumber().getIban());

    }


    public SavingsAccount createSavingsAccount(User user, CreateSavingsAccountDto createSavingsAccountDto)
            throws IronbankAccountException {

        if (createSavingsAccountDto.getInterestRate() != null
                && createSavingsAccountDto.getInterestRate() > SavingsAccount.MAX_INTEREST_RATE) {
            throw new IronbankAccountException(
                    "Unexpected interest rate. Must be greater than %s".formatted(SavingsAccount.MAX_INTEREST_RATE));
        } else if (createSavingsAccountDto.getInterestRate() == null) {
            createSavingsAccountDto.setInterestRate(SavingsAccount.DEFAULT_INTEREST_RATE);
        }

        if (createSavingsAccountDto.getMinimumBalance() != null && createSavingsAccountDto.getMinimumBalance()
                .getAmount().compareTo(SavingsAccount.MIN_MINIMUM_BALANCE.getAmount()) < 0) {
            throw new IronbankAccountException("Unexpected minimum balance. Must be greater than %s"
                    .formatted(SavingsAccount.MIN_MINIMUM_BALANCE));
        } else if (createSavingsAccountDto.getMinimumBalance() == null) {
            createSavingsAccountDto.setMinimumBalance(SavingsAccount.MIN_MINIMUM_BALANCE);
        }

        if (createSavingsAccountDto.getBalance().getAmount()
                .compareTo(createSavingsAccountDto.getMinimumBalance().getAmount()) < 0) {
            throw new IronbankAccountException("Balance too low");
        }

        var primaryOwner = userRepository.findById(createSavingsAccountDto.getPrimaryOwnerId()).orElseThrow();

        if (primaryOwner.getUserRole() != UserRole.ACCOUNT_HOLDER) {
            throw new IronbankAccountException("Only Account Holders can open accounts");
        }

        User secondaryOwner = null;

        if(createSavingsAccountDto.getSecondaryOwnerId() != null) {
            var secondaryOwnerOpt = userRepository.findById(createSavingsAccountDto.getSecondaryOwnerId());

            if (secondaryOwnerOpt.isPresent() && secondaryOwnerOpt.get().getUserRole() != UserRole.ACCOUNT_HOLDER) {
                throw new IronbankAccountException("Only Account Holders can open accounts");
            } else if(secondaryOwnerOpt.isPresent()) {
                secondaryOwner = secondaryOwnerOpt.get();
            }
        }

        AccountNumber accountNumber = new AccountNumber(Iban.random().toString());
        accountNumberRepository.save(accountNumber);

        var account = new SavingsAccount(
                accountNumber,
                primaryOwner,
                secondaryOwner,
                AccountStatus.ACTIVE,
                createSavingsAccountDto.getSecretKey(),
                createSavingsAccountDto.getMinimumBalance(),
                createSavingsAccountDto.getInterestRate());

        account = savingsAccountRepository.save(account);

        transactionService.logAccountCreated(account, createSavingsAccountDto.getBalance().getAmount(), user);

        return (SavingsAccount) getAccountByAccountNumber(account.getAccountNumber().getIban());

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
        if (accountNumberFound.isEmpty()) {
            throw new IronbankAccountException("Unexpected account number");
        }

        Account account;
        account = checkingAccountRepository.findByAccountNumber(accountNumberFound.get());
        if (account == null) {
            account = savingsAccountRepository.findByAccountNumber(accountNumberFound.get());
        }
        if (account == null) {
            account = creditAccountRepository.findByAccountNumber(accountNumberFound.get());
        }
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return account;
    }

    public Account closeAccount(String accountId, User user) throws IronbankAccountException {
        var account = getAccountByAccountNumber(accountId);
        if(account.getBalance().equals(new BigDecimal(0))){
            account.setStatus(AccountStatus.CLOSED);
        }
        if(account instanceof CheckingAccount) {
            checkingAccountRepository.save((CheckingAccount) account);
        }else if(account instanceof SavingsAccount) {
            savingsAccountRepository.save((SavingsAccount) account);
        }else if(account instanceof CreditAccount) {
            creditAccountRepository.save((CreditAccount) account);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        transactionService.logAccountClosed(account, user);

        return account;
    }
}
