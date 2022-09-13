package com.ironhack.ironbankapi.accounts.repository;

import com.ironhack.ironbankapi.core.model.account.StudentCheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCheckingAccountRepository extends JpaRepository<StudentCheckingAccount, String> {
}
