package com.ironhack.ironbankapi.core.repository.account;

import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.account.AccountNumber;
import com.ironhack.ironbankapi.core.model.account.SavingsAccount;
import com.ironhack.ironbankapi.core.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, String> {
    List<SavingsAccount> findByPrimaryOwner(User user);

    Collection<? extends SavingsAccount> findBySecondaryOwner(User user);

    Account findByAccountNumber(AccountNumber accountNumber);
}
