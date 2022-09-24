package com.ironhack.ironbankapi.core.dto;

import java.math.BigDecimal;
import java.time.Instant;

public interface AccountBalanceDto {

    String getAccountNumber();

    BigDecimal getBalance();

    Instant getLastTransactionTime();

}
