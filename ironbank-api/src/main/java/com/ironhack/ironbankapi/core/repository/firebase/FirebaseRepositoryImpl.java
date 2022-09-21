package com.ironhack.ironbankapi.core.repository.firebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.ironhack.ironbankapi.core.dto.FirebaseLoginRequest;
import com.ironhack.ironbankapi.core.dto.FirebaseLoginResponse;
import com.ironhack.ironbankapi.core.model.user.UserRole;

@Repository
public class FirebaseRepositoryImpl implements FirebaseRepository {

  private final FirebaseProperties firebaseProperties;
  private final WebClient webClient;

  public FirebaseRepositoryImpl(WebClient.Builder webClientBuilder, FirebaseProperties firebaseProperties)
      throws IOException {
    this.firebaseProperties = firebaseProperties;
    this.webClient = webClientBuilder.build();
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

  @Override
  public String getToken(String email, String password) {
    System.out.println(firebaseProperties);
    var response = webClient
        .post()
        .uri(URI.create(firebaseProperties.getLoginUrl() + "?key=" + firebaseProperties.getApiKey()))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(new FirebaseLoginRequest(email, password)))
        .retrieve()
        .bodyToMono(FirebaseLoginResponse.class)
        .block();
    return response.getIdToken();
  }

}
