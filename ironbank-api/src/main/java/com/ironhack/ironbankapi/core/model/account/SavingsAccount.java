package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = SavingsAccount.TABLE_NAME)
@SQLDelete(sql = "UPDATE "+SavingsAccount.TABLE_NAME+" SET deletedAt = SYSDATE() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SavingsAccount extends Account {

    public static final String TABLE_NAME = "savings_account";

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance"))
    private Money minimumBalance;
    private double interestRate;

}
