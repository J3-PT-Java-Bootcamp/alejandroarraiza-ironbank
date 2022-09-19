package com.ironhack.ironbankapi.accounts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@Setter
public class TransactionThirdPartyTransferRequestDto extends TransactionRequestDto {

    @NotEmpty
    @Length(min = 24, max = 24)
    private String externalAccountHash;

    private String originAccountNumber;

    private String destinationAccountNumber;

    public TransactionThirdPartyTransferRequestDto(BigDecimal amount, String externalAccountHash,
            String originAccountNumber, String destinationAccountNumber) {
        super(amount);
        this.externalAccountHash = externalAccountHash;
        this.originAccountNumber = originAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
    }
}
