package com.ironhack.ironbankapi.accounts.dto;

import com.ironhack.ironbankapi.core.model.common.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateCheckingAccountDto {

    private Money balance;

    private String primaryOwnerId;

    private String secondaryOwnerId;

    private String secretKey;

}