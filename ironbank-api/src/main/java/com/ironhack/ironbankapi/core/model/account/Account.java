package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class Account {

    public static final Money PENALTY_FEE = new Money(new BigDecimal("40"));

    @OneToOne
    @JoinColumn(name = "account_number_iban")
    private AccountNumber accountNumber;

    @Formula("(SELECT sum(t.amount) from transaction t where t.account_number_iban_destination = account_number_iban and t.transaction_result = 'EXECUTED')")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    private User primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    private User secondaryOwner;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant deletedAt;

    private String secretKey;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public Account(AccountNumber accountNumber, User primaryOwner, User secondaryOwner, AccountStatus status,
            String secretKey, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.status = status;
        this.secretKey = secretKey;
        this.accountType = accountType;
    }

    public boolean checkUserIsOwner(User user) {
        return primaryOwner.getId().equals(user.getId())
                || (secondaryOwner != null && secondaryOwner.getId().equals(user.getId()));
    }

    public abstract boolean validateNewBalance(BigDecimal newBalance);
}
