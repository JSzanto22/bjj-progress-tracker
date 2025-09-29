package com.bjjnotetaker.bjjnotetaker.controllers;

import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.dto.authentication.LoginRequestDTO;
import com.bjjnotetaker.bjjnotetaker.dto.authentication.LoginResponseDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserRegisterDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserResponseDTO;
import com.bjjnotetaker.bjjnotetaker.mappers.UserMapper;
import com.bjjnotetaker.bjjnotetaker.service.AuthService;
import com.bjjnotetaker.bjjnotetaker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private UserService userService;

  @Autowired
  UserMapper userMapper;

  @PostMapping("/login")
  public LoginResponseDTO login(@RequestBody LoginRequestDTO request){
    User user = authService.authenticate(request.getUsername(),request.getPlaintextPassword());

    String jwt = authService.generateToken(user.getId(),user.getUsername());

    return new LoginResponseDTO(user.getId(), user.getUsername(), jwt);
  }

  @PostMapping("/register")
  public UserResponseDTO register(@RequestBody UserRegisterDTO request){
    User user = userMapper.mapUser(request);

    try{
      userService.getUserByUsername(user.getUsername());
    } catch (EntityNotFoundException e) {
      userService.registerUser(user);
      return userMapper.mapUser(user);
    }

    throw new RuntimeException("Username already exists!");

  }
}
