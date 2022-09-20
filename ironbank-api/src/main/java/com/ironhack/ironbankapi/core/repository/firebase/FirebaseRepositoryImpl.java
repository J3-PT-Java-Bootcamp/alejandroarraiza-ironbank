package com.ironhack.ironbankapi.core.repository.firebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.ironhack.ironbankapi.core.model.user.UserRole;

import antlr.collections.List;

@Repository
public class FirebaseRepositoryImpl implements FirebaseRepository {

  public FirebaseRepositoryImpl() throws IOException {
    init();
  }

  void init() throws IOException {

    FileInputStream serviceAccount = new FileInputStream(
        "resources/ironbank-b9a27-firebase-adminsdk-iv02l-ab4cbd704b.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();

    FirebaseApp.initializeApp(options);

  }

  public String createUser(String email, String password, UserRole role) throws FirebaseAuthException {
    CreateRequest request = new CreateRequest()
        .setEmail(email)
        .setPassword(password);

    UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

    var permissions = new ArrayList<String>();
    permissions.add(role.toString());

    Map<String, Object> claims = Map.of("custom_claims", permissions);

    FirebaseAuth.getInstance().setCustomUserClaims(userRecord.getUid(), claims);

    return userRecord.getUid();
  }

}
