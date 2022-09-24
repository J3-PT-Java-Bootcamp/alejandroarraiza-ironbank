package com.ironhack.ironbankapi.core.model.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
@ToString
@EqualsAndHashCode
public class User {

        /**
         * This field will be the security provider user id
         */
        @Id
        @Column(nullable = false)
        private String id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String email;

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        private UserRole userRole;

        @Column
        private LocalDate dateOfBirth;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "street", column = @Column(name = "primary_street")),
                        @AttributeOverride(name = "postalCode", column = @Column(name = "primary_postal_code")),
                        @AttributeOverride(name = "city", column = @Column(name = "primary_city")),
                        @AttributeOverride(name = "state", column = @Column(name = "primary_state")),
                        @AttributeOverride(name = "country", column = @Column(name = "primary_country")),
        })
        private Address primaryAddress;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "street", column = @Column(name = "secondary_street")),
                        @AttributeOverride(name = "postalCode", column = @Column(name = "secondary_postal_code")),
                        @AttributeOverride(name = "city", column = @Column(name = "secondary_city")),
                        @AttributeOverride(name = "state", column = @Column(name = "secondary_state")),
                        @AttributeOverride(name = "country", column = @Column(name = "secondary_country")),
        })
        private Address secondaryAddress;

        private String externalAccount;

}
