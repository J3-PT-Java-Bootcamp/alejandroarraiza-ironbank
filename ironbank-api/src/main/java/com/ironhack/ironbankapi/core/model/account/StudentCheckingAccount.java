package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import lombok.*;

import javax.persistence.Entity;
import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class StudentCheckingAccount extends Account {

    public StudentCheckingAccount(String accountNumber, Money balance, User primaryOwner, User secondayOwner, Money penaltyFee, AccountStatus status, Instant creationDate, String secretKey) {
        super(accountNumber, balance, primaryOwner, secondayOwner, penaltyFee, status, creationDate, secretKey);
    }
}
