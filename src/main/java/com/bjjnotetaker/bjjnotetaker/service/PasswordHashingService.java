package com.bjjnotetaker.bjjnotetaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordHashingService {

  private final PasswordEncoder passwordEncoder;

  public PasswordHashingService() {
    this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  public String hashPassword(String plaintextPassword) {
    return passwordEncoder.encode(plaintextPassword);
  }

  public boolean passwordsMatch(String plaintextPassword, String hashedPassword) {
    return passwordEncoder.matches(plaintextPassword, hashedPassword);
  }



}
