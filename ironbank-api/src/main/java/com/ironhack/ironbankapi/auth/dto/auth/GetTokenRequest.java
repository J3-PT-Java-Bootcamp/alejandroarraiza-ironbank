package com.ironhack.ironbankapi.auth.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetTokenRequest {

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;

}
