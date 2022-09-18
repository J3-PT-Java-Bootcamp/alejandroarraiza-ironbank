package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.accounts.exception.IronbankAccountException;
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CheckingAccountType checkingAccountType;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance"))
    private Money minimumBalance;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee"))
    private Money monthlyMaintenanceFee;

    public CheckingAccount(AccountNumber accountNumber, User primaryOwner, User secondaryOwner, AccountStatus status, String secretKey, CheckingAccountType checkingAccountType, Money minimumBalance, Money monthlyMaintenanceFee) {
        super(accountNumber, primaryOwner, secondaryOwner, status, secretKey);
        this.checkingAccountType = checkingAccountType;
        this.minimumBalance = minimumBalance;
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }
}
