package com.ironhack.ironbankapi.core.repository.account;

import com.ironhack.ironbankapi.core.model.account.AccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountNumberRepository extends JpaRepository<AccountNumber, String> {
}
