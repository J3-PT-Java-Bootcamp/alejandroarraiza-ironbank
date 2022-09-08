package com.ironhack.ironbankapi.auth.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateThirdPartyDto extends CreateUserDto{

    @NotEmpty
    @Length(min = 24, max = 24)
    private String externalAccount;

}
