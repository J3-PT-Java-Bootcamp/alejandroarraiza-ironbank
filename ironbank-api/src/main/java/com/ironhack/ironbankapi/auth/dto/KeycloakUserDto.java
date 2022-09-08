package com.ironhack.ironbankapi.auth.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KeycloakUserDto {

    private String id;

    private String userName;
    private String password;

}
