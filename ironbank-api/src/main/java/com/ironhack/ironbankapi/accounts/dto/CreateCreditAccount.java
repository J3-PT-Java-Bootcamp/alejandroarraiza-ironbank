package com.ironhack.ironbankapi.accounts.dto;

import com.ironhack.ironbankapi.core.model.common.Money;

public class CreateCreditAccount {

    private Money balance;

    private String primaryOwnerId;

    private String secondaryOwnerId;

    private String secretKey;

    private Money creditLimit;
}
