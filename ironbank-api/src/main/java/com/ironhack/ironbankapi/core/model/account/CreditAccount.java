package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreditAccount extends Account{
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "credit_limit"))
    private Money creditLimit;
    private double interestRate;
}
