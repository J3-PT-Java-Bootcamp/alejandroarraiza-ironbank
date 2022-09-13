package com.ironhack.ironbankapi.accounts.repository;

import com.ironhack.ironbankapi.core.model.account.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, String> {
}
