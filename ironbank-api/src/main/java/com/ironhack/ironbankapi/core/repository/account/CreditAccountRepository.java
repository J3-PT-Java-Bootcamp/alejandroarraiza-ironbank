package com.ironhack.ironbankapi.core.repository.account;

import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.account.AccountNumber;
import com.ironhack.ironbankapi.core.model.account.CreditAccount;
import com.ironhack.ironbankapi.core.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CreditAccountRepository extends JpaRepository<CreditAccount, String> {
    List<CreditAccount> findByPrimaryOwner(User user);

    Collection<? extends CreditAccount> findBySecondaryOwner(User user);

    Account findByAccountNumber(AccountNumber accountNumber);
}
