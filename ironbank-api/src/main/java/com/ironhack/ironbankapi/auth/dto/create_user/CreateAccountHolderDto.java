package com.ironhack.ironbankapi.auth.dto.create_user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateAccountHolderDto extends CreateUserDto{

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate dateOfBirth;

    @NotEmpty
    private String primaryStreet;

    @NotEmpty
    private String primaryPostalCode;

    @NotEmpty
    private String primaryCity;

    @NotEmpty
    private String primaryState;

    @NotEmpty
    private String primaryCountry;

    private String secondaryStreet;
    private String secondaryPostalCode;
    private String secondaryCity;
    private String secondaryState;
    private String secondaryCountry;

}
