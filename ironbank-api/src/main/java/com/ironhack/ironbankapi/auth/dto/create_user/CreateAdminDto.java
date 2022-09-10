package com.ironhack.ironbankapi.auth.dto.create_user;

import com.ironhack.ironbankapi.auth.model.UserRole;
import lombok.*;

import javax.validation.constraints.NotEmpty;


@NoArgsConstructor
@Getter
@Setter
public class CreateAdminDto extends CreateUserDto{

    public CreateAdminDto(@NotEmpty String name, UserRole userRole) {
        super(name, userRole);
    }

}
