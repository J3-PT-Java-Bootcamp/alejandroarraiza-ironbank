package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = CreditAccount.TABLE_NAME)
@SQLDelete(sql = "UPDATE "+CreditAccount.TABLE_NAME+" SET deletedAt = SYSDATE() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreditAccount extends Account{

    public static final String TABLE_NAME = "credit_account";

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "credit_limit"))
    private Money creditLimit;
    private double interestRate;
}
