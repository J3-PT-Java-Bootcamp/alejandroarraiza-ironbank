package com.ironhack.ironbankapi.accounts.dto;

import com.ironhack.ironbankapi.core.model.common.Money;

public class CreateCheckingAccountDto {

    private Money balance;

    private String primaryOwnerId;

    private String secondaryOwnerId;

    private String secretKey;

}
