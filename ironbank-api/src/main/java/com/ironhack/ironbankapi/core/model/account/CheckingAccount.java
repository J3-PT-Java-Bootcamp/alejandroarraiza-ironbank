package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = CheckingAccount.TABLE_NAME)
@SQLDelete(sql = "UPDATE "+CheckingAccount.TABLE_NAME+" SET deletedAt = SYSDATE() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
public class CheckingAccount extends Account{

    public static final String TABLE_NAME = "checking_account";

    public static final Money MINIMUM_BALANCE = new Money(new BigDecimal("250"));

    public static final Money MONTHLY_MAINTENANCE_FEE = new Money(new BigDecimal("12"));

    public static final int STUDENT_AGE_LIMIT = 24;

    @Enumerated(EnumType.STRING)
    private CheckingAccountType checkingAccountType;

    public CheckingAccount(String accountNumber, Money balance, User primaryOwner, User secondaryOwner, AccountStatus status, String secretKey, CheckingAccountType checkingAccountType) {
        super(accountNumber, balance, primaryOwner, secondaryOwner, status, secretKey);
        this.checkingAccountType = checkingAccountType;
    }
}
