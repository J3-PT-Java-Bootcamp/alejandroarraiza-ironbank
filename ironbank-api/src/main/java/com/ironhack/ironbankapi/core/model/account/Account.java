package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class Account {

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
    @JoinColumn(name = "seconday_owner_id")
    private User secondayOwner;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "penalty_fee"))
    private Money penaltyFee;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private Instant creationDate;
    private String secretKey;

}
