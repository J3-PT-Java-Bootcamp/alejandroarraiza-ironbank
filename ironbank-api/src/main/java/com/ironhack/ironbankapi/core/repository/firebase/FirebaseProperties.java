package com.ironhack.ironbankapi.core.repository.firebase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Configuration
@PropertySource("classpath:firebase.properties")
@ConfigurationProperties("firebase")
public class FirebaseProperties {

  private String loginUrl;

  private String apiKey;

}
