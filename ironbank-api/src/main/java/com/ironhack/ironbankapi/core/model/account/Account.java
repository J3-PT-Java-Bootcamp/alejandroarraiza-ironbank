package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class Account {

    public static final Money PENALTY_FEE = new Money(new BigDecimal("40"));

    @Id
    @Column(nullable = false)
    private String accountNumber;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "balance"))
    private Money balance;

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

    public Account(String accountNumber, Money balance, User primaryOwner, User secondaryOwner, AccountStatus status, String secretKey) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.status = status;
        this.secretKey = secretKey;
    }
}
