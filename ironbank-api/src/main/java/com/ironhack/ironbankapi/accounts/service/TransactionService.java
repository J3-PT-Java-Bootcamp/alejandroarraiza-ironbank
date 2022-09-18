package com.ironhack.ironbankapi.accounts.service;

import com.ironhack.ironbankapi.accounts.dto.TransactionResultDto;
import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.transaction.Transaction;
import com.ironhack.ironbankapi.core.model.transaction.TransactionResult;
import com.ironhack.ironbankapi.core.model.transaction.TransactionType;
import com.ironhack.ironbankapi.core.repository.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResultDto logAccountCreated(Account account, BigDecimal amount) {

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.OPEN_ACCOUNT)
                .transactionResult(TransactionResult.EXECUTED)
                .accountNumberDestination(account.getAccountNumber())
                .amount(new Money(amount))
                .build();

        transactionRepository.save(transaction);

        return new TransactionResultDto(transaction.getTransactionResult());

    }

    public TransactionResultDto updateBalance(Account account, BigDecimal amount) {

        BigDecimal balanceDiff = amount.subtract(account.getBalance());

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.ADMIN)
                .transactionResult(TransactionResult.EXECUTED)
                .accountNumberDestination(account.getAccountNumber())
                .amount(new Money(balanceDiff))
                .build();

        transactionRepository.save(transaction);

        return new TransactionResultDto(transaction.getTransactionResult());

    }

    public TransactionResultDto withdraw(Account account, BigDecimal amount) {

        // TODO validation

        TransactionResult transactionResult = TransactionResult.REJECTED;

        if (amount.compareTo(account.getBalance()) <= 0) {
            transactionResult = TransactionResult.EXECUTED;
        }

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.WITHDRAW)
                .transactionResult(transactionResult)
                .accountNumberDestination(account.getAccountNumber())
                .amount(new Money(amount.negate()))
                .build();

        transactionRepository.save(transaction);

        return new TransactionResultDto(transactionResult);
    }

    public TransactionResultDto deposit(Account account, BigDecimal amount) {

        // TODO validation

        TransactionResult transactionResult = TransactionResult.REJECTED;

        if (amount.compareTo(account.getBalance()) <= 0) {
            transactionResult = TransactionResult.EXECUTED;
        }

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .transactionResult(transactionResult)
                .accountNumberDestination(account.getAccountNumber())
                .amount(new Money(amount))
                .build();

        transactionRepository.save(transaction);

        return new TransactionResultDto(transactionResult);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
