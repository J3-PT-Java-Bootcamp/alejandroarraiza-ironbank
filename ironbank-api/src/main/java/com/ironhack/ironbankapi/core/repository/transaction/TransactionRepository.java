package com.ironhack.ironbankapi.core.repository.transaction;

import com.ironhack.ironbankapi.core.model.transaction.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  // @Query(value = "SELECT account_number_iban_destination as account_number,
  // sum(amount) as balance, max(created_at) as last_transaction_time from
  // Transaction where account_number_iban_destination = ?1 and
  // transaction_result= ?2", nativeQuery = true)
  // Optional<AccountBalanceDto> getAccountBalanceSum(String accountNumber, String
  // transactionResult);

}
