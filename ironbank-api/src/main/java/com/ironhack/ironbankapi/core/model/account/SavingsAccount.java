package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = SavingsAccount.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SavingsAccount extends Account {

    public static final String TABLE_NAME = "savings_account";

    public static final double DEFAULT_INTEREST_RATE = 0.0025;
    public static final double MAX_INTEREST_RATE = 0.5;
    public static final Money DEFAULT_MINIMUM_BALANCE = new Money(new BigDecimal("1000"));
    public static final Money MIN_MINIMUM_BALANCE = new Money(new BigDecimal("100"));

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance"))
    private Money minimumBalance;
    private double interestRate;

    public SavingsAccount(AccountNumber accountNumber, User primaryOwner, User secondaryOwner, AccountStatus status,
            String secretKey, Money minimumBalance, double interestRate) {
        super(accountNumber, primaryOwner, secondaryOwner, status, secretKey, AccountType.SAVINGS);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    @Override
    public boolean validateNewBalance(BigDecimal newBalance) {
        boolean isValid = true;

        if (newBalance.compareTo(this.getMinimumBalance().getAmount()) < 0) {
            return false;
        }

        return isValid;
    }
}
