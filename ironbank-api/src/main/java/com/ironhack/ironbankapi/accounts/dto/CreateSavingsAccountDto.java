package com.ironhack.ironbankapi.accounts.dto;

import com.ironhack.ironbankapi.core.model.common.Money;

public class CreateSavingsAccountDto {

    private Money balance;

    private String primaryOwnerId;

    private String secondaryOwnerId;

    private String secretKey;

}
