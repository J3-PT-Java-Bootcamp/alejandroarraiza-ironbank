package com.ironhack.ironbankapi.core.repository.account;

import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.account.AccountNumber;
import com.ironhack.ironbankapi.core.model.account.CheckingAccount;
import com.ironhack.ironbankapi.core.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, String> {
    List<CheckingAccount> findByPrimaryOwner(User userId);

    List<CheckingAccount> findBySecondaryOwner(User userId);

    Account findByAccountNumber(AccountNumber accountNumber);
}
