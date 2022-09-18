package com.ironhack.ironbankapi.core.dto;

import com.ironhack.ironbankapi.core.model.account.AccountNumber;
import com.ironhack.ironbankapi.core.model.common.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;


public interface AccountBalanceDto {

    String getAccountNumber();

    BigDecimal getBalance();

    Instant getLastTransactionTime();

}
