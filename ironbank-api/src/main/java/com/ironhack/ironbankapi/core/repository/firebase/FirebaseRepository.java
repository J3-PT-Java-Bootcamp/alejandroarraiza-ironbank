package com.ironhack.ironbankapi.core.repository.firebase;

import com.google.firebase.auth.FirebaseAuthException;
import com.ironhack.ironbankapi.core.model.user.UserRole;

public interface FirebaseRepository {

  String createUser(String email, String password, UserRole role) throws FirebaseAuthException;

  String getToken(String email, String password);

}
