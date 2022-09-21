package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = CreditAccount.TABLE_NAME)
@SQLDelete(sql = "UPDATE " + CreditAccount.TABLE_NAME + " SET deletedAt = SYSDATE() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
public class CreditAccount extends Account {

    public static final String TABLE_NAME = "credit_account";

    public static final Money DEFAULT_CREDIT_LIMIT = new Money(new BigDecimal("100"));
    public static final Money MAX_CREDIT_LIMIT = new Money(new BigDecimal("100000"));
    public static final double DEFAULT_INTEREST_RATE = 0.2;
    public static final double MIN_INTEREST_RATE = 0.1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "credit_limit"))
    private Money creditLimit;
    private double interestRate;

    public CreditAccount(AccountNumber accountNumber, User primaryOwner, User secondaryOwner, AccountStatus status,
            String secretKey, Money creditLimit, double interestRate) {
        super(accountNumber, primaryOwner, secondaryOwner, status, secretKey, AccountType.CREDIT);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }
}
