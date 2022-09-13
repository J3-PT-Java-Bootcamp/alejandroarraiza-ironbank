package com.ironhack.ironbankapi.accounts.repository;

import com.ironhack.ironbankapi.core.model.account.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, String> {
}
