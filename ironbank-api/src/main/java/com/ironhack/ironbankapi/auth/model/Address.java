package com.ironhack.ironbankapi.auth.model;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Address {
    private String street;
    private String postalCode;
    private String city;
    private String state;
    private String country;
}
