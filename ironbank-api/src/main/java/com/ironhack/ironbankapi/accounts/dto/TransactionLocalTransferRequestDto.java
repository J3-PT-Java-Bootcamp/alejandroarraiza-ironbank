package com.ironhack.ironbankapi.accounts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class TransactionLocalTransferRequestDto extends TransactionRequestDto {

    private String originAccountNumber;

    private String destinationAccountNumber;

    public TransactionLocalTransferRequestDto(BigDecimal amount, String secretKey, String originAccountNumber,
            String destinationAccountNumber) {
        super(amount, secretKey);
        this.originAccountNumber = originAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
    }
}
