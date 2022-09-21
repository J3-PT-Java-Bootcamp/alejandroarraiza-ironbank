package com.ironhack.ironbankapi.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FirebaseLoginRequest {

  private String email;

  private String password;

  private boolean returnSecureToken = true;

  public FirebaseLoginRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

}
