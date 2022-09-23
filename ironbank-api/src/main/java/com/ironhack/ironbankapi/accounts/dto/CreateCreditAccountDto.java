package com.ironhack.ironbankapi.accounts.dto;

import com.ironhack.ironbankapi.core.model.common.Money;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateCreditAccountDto {

    private Money balance;

    private String primaryOwnerId;

    private String secondaryOwnerId;

    private String secretKey;

    private Money creditLimit;

    private Double interestRate;
}
