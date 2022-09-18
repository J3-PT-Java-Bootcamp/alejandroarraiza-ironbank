package com.ironhack.ironbankapi.core.repository.transaction;

import com.ironhack.ironbankapi.core.dto.AccountBalanceDto;
import com.ironhack.ironbankapi.core.model.transaction.Transaction;
import com.ironhack.ironbankapi.core.model.transaction.TransactionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

//    @Query(value = "SELECT account_number_iban_destination as accountNumber, sum(amount) as balance, max(created_at) as lastTransactionTime from Transaction where account_number_iban_destination = ?1 and transaction_result = ?2", nativeQuery = true)
//    AccountBalanceDto getAccountBalanceSum(String accountNumber, String transactionResult);

}
