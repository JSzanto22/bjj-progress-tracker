package com.bjjnotetaker.bjjnotetaker.service;

import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class AuthService {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordHashingService passwordHashingService;

  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private final long expirationMillis = 36000000; // 10 hours

  public String generateToken(Long userId, String username) {
    return Jwts.builder()
      .setSubject(username)
      .claim("userId", userId)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
      .signWith(key)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Long getUserIdFromToken(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .getBody()
      .get("userId", Long.class);
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }


  public User authenticate(String username, String rawPassword){
    User user;
    try {
      user = userService.getUserByUsername(username);
    } catch (Exception e) {
      throw new RuntimeException("Invalid username or password");
    }

    if(!passwordHashingService.passwordsMatch(rawPassword, user.getPasswordHash())){
      throw new RuntimeException("Invalid username or password");
    }

    return user;
  }

}
