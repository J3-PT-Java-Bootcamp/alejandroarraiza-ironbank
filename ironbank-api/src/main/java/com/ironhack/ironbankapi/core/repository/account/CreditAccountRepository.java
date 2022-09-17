package com.ironhack.ironbankapi.core.repository.account;

import com.ironhack.ironbankapi.core.model.account.CreditAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditAccountRepository extends JpaRepository<CreditAccount, String> {
}
