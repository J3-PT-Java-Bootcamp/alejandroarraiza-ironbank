package com.ironhack.ironbankapi.accounts.service;

import com.ironhack.ironbankapi.accounts.dto.TransactionResultDto;
import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.account.AccountStatus;
import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.transaction.Transaction;
import com.ironhack.ironbankapi.core.model.transaction.TransactionResult;
import com.ironhack.ironbankapi.core.model.transaction.TransactionType;
import com.ironhack.ironbankapi.core.model.user.User;
import com.ironhack.ironbankapi.core.repository.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResultDto logAccountCreated(Account account, BigDecimal amount, User user) {

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.OPEN_ACCOUNT)
                .transactionResult(TransactionResult.EXECUTED)
                .accountNumberDestination(account.getAccountNumber())
                .amount(new Money(amount))
                .user(user)
                .build();

        transactionRepository.save(transaction);

        return new TransactionResultDto(transaction.getTransactionResult());

    }

    public TransactionResultDto logAccountClosed(Account account, User user) {

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.CLOSE_ACCOUNT)
                .transactionResult(TransactionResult.EXECUTED)
                .accountNumberDestination(account.getAccountNumber())
                .user(user)
                .build();

        transactionRepository.save(transaction);

        return new TransactionResultDto(transaction.getTransactionResult());

    }

    public TransactionResultDto updateBalance(Account account, BigDecimal amount, User user) {

        BigDecimal balanceDiff = amount.subtract(account.getBalance());

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.ADMIN)
                .transactionResult(TransactionResult.EXECUTED)
                .accountNumberDestination(account.getAccountNumber())
                .amount(new Money(balanceDiff))
                .user(user)
                .build();

        transactionRepository.save(transaction);

        return new TransactionResultDto(transaction.getTransactionResult());

    }

    public TransactionResultDto withdraw(Account account, BigDecimal amount, User user, String secretKey) {

        TransactionResult transactionResult = TransactionResult.REJECTED;

        if (account.checkUserIsOwner(user) && amount.compareTo(account.getBalance()) <= 0
                && account.getStatus() == AccountStatus.ACTIVE
                && account.getSecretKey().equals(secretKey)) {
            transactionResult = TransactionResult.EXECUTED;
        }

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.WITHDRAW)
                .transactionResult(transactionResult)
                .accountNumberDestination(account.getAccountNumber())
                .amount(new Money(amount.negate()))
                .user(user)
                .build();

        transactionRepository.save(transaction);

        return new TransactionResultDto(transactionResult);
    }

    public TransactionResultDto deposit(Account account, BigDecimal amount, User user, String secretKey) {

        TransactionResult transactionResult = TransactionResult.REJECTED;

        if (account.checkUserIsOwner(user) && amount.compareTo(account.getBalance()) <= 0
                && account.getStatus() == AccountStatus.ACTIVE
                && account.getSecretKey().equals(secretKey)) {
            transactionResult = TransactionResult.EXECUTED;
        }

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .transactionResult(transactionResult)
                .accountNumberDestination(account.getAccountNumber())
                .amount(new Money(amount))
                .user(user)
                .build();

        transactionRepository.save(transaction);

        return new TransactionResultDto(transactionResult);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public TransactionResultDto localTransfer(Account origin, Account destination, BigDecimal amount, User user,
            String secretKey) {

        TransactionResult transactionResult = TransactionResult.REJECTED;

        if (origin.getBalance().compareTo(amount) >= 0
                && origin.checkUserIsOwner(user)
                && origin.getStatus() == AccountStatus.ACTIVE
                && destination.getStatus() != AccountStatus.FROZEN
                && origin.getSecretKey().equals(secretKey)) {
            transactionResult = TransactionResult.EXECUTED;
        }

        Transaction transactionDestination = Transaction.builder()
                .transactionType(TransactionType.LOCAL_TRANSFER)
                .transactionResult(transactionResult)
                .accountNumberOrigin(origin.getAccountNumber())
                .accountNumberDestination(destination.getAccountNumber())
                .amount(new Money(amount))
                .user(user)
                .build();

        Transaction transactionOrigin = Transaction.builder()
                .transactionType(TransactionType.LOCAL_TRANSFER)
                .transactionResult(transactionResult)
                .accountNumberOrigin(destination.getAccountNumber())
                .accountNumberDestination(origin.getAccountNumber())
                .amount(new Money(amount.negate()))
                .user(user)
                .build();

        transactionRepository.save(transactionDestination);
        transactionRepository.save(transactionOrigin);

        return new TransactionResultDto(transactionResult);
    }

    @Transactional
    public TransactionResultDto thirdPartyTransfer(String externalAccountHash, Account origin, Account destination,
            BigDecimal amount, User user, String secretKey) {

        TransactionResult transactionResult = TransactionResult.REJECTED;
        Transaction transaction = null;
        if (origin != null) {
            if (origin.getBalance().compareTo(amount) >= 0 && origin.getStatus() == AccountStatus.ACTIVE
                    && origin.getSecretKey().equals(secretKey) && externalAccountHash != null) {
                transactionResult = TransactionResult.EXECUTED;
            }

            transaction = Transaction.builder()
                    .transactionType(TransactionType.THIRD_PARTY_TRANSFER)
                    .transactionResult(transactionResult)
                    .accountNumberDestination(origin.getAccountNumber())
                    .amount(new Money(amount.negate()))
                    .user(user)
                    .externalAccountHash(externalAccountHash)
                    .build();
        }
        if (destination != null) {

            if (destination.getStatus() == AccountStatus.ACTIVE
                    && externalAccountHash.equals(user.getExternalAccount())) {
                transactionResult = TransactionResult.EXECUTED;
            }

            transaction = Transaction.builder()
                    .transactionType(TransactionType.THIRD_PARTY_TRANSFER)
                    .transactionResult(transactionResult)
                    .accountNumberDestination(destination.getAccountNumber())
                    .amount(new Money(amount))
                    .user(user)
                    .externalAccountHash(externalAccountHash)
                    .build();
        }

        transactionRepository.save(transaction);

        return new TransactionResultDto(transactionResult);
    }
}
