package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = CheckingAccount.TABLE_NAME)
@SQLDelete(sql = "UPDATE "+CheckingAccount.TABLE_NAME+" SET deletedAt = SYSDATE() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckingAccount extends Account{

    public static final String TABLE_NAME = "checking_account";

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance"))
    private Money minimumBalance;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee"))
    private Money monthlyMaintenanceFee;

}
