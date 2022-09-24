package com.ironhack.ironbankapi.accounts.schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ironhack.ironbankapi.accounts.service.AccountService;
import com.ironhack.ironbankapi.accounts.service.TransactionService;
import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.account.AccountStatus;

@Component
public class AccountScheduler {

  @Autowired
  AccountService accountService;

  @Autowired
  TransactionService transactionService;

  public void penaltyFees() {

    List<Account> accounts = new ArrayList<>();

    accounts.addAll(accountService.getAllCheckingAccounts());
    accounts.addAll(accountService.getAllCreditAccounts());
    accounts.addAll(accountService.getAllSavingsAccounts());

    accounts.removeIf((account) -> account.getStatus() != AccountStatus.ACTIVE);

    accounts.forEach((account) -> transactionService.checkAndApplyPenaltyFee(account));

  }

  @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.DAYS)
  public void interests() {

    var creditAccounts = accountService.getAllCreditAccounts();
    var savingsAccounts = accountService.getAllSavingsAccounts();

    creditAccounts.removeIf((account) -> account.getStatus() != AccountStatus.ACTIVE);
    savingsAccounts.removeIf((account) -> account.getStatus() != AccountStatus.ACTIVE);

    creditAccounts.forEach((account) -> transactionService.applyCreditInterestRate(account));
    savingsAccounts.forEach((account) -> transactionService.applySavingsInterestRate(account));

  }

}
