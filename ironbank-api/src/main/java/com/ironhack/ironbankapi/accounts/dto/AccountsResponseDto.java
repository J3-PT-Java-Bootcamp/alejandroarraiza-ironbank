package com.ironhack.ironbankapi.accounts.dto;

import com.ironhack.ironbankapi.core.model.account.CheckingAccount;
import com.ironhack.ironbankapi.core.model.account.CreditAccount;
import com.ironhack.ironbankapi.core.model.account.SavingsAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AccountsResponseDto {

    private List<CheckingAccount> checkingAccountList;
    private List<SavingsAccount> savingsAccountList;
    private List<CreditAccount> creditAccountList;

}
