package com.ironhack.ironbankapi.core.model.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountNumber {

    @Id
    @Column(nullable = false)
    private String iban;

}
