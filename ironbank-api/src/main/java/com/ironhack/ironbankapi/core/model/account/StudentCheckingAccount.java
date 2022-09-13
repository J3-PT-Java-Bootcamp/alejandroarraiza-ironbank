package com.ironhack.ironbankapi.core.model.account;

import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = StudentCheckingAccount.TABLE_NAME)
@SQLDelete(sql = "UPDATE "+StudentCheckingAccount.TABLE_NAME+" SET deletedAt = SYSDATE() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
public class StudentCheckingAccount extends Account {

    public static final String TABLE_NAME = "student_checking_account";

    public StudentCheckingAccount(String accountNumber, Money balance, User primaryOwner, User secondaryOwner, Money penaltyFee, AccountStatus status, String secretKey) {
        super(accountNumber, balance, primaryOwner, secondaryOwner, penaltyFee, status, secretKey);
    }
}
