package com.ironhack.ironbankapi.accounts.dto;

import com.ironhack.ironbankapi.core.model.transaction.TransactionResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionResultDto {

    private TransactionResult transactionResult;

}
