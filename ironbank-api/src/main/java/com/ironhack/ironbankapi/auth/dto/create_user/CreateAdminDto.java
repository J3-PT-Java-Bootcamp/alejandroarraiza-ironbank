package com.ironhack.ironbankapi.auth.dto.create_user;

import com.ironhack.ironbankapi.auth.model.UserRole;
import lombok.*;

import javax.validation.constraints.NotEmpty;


@NoArgsConstructor
@Getter
@Setter
public class CreateAdminDto extends CreateUserDto{

    public CreateAdminDto(@NotEmpty String name, @NotEmpty String email, UserRole userRole) {
        super(name, email, userRole);
    }
}
