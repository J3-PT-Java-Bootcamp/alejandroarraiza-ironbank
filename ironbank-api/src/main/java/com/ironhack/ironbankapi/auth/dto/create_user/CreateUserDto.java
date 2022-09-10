package com.ironhack.ironbankapi.auth.dto.create_user;


import com.ironhack.ironbankapi.auth.model.UserRole;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class CreateUserDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    private UserRole userRole;

}
