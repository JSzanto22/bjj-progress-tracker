package com.bjjnotetaker.bjjnotetaker.controllers;

import ch.qos.logback.core.LayoutBase;
import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.dto.ErrorResponseDTO;
import com.bjjnotetaker.bjjnotetaker.dto.authentication.LoginRequestDTO;
import com.bjjnotetaker.bjjnotetaker.dto.authentication.LoginResponseDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserRegisterDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserResponseDTO;
import com.bjjnotetaker.bjjnotetaker.mappers.UserMapper;
import com.bjjnotetaker.bjjnotetaker.service.AuthService;
import com.bjjnotetaker.bjjnotetaker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

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
  public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request){
    User user = authService.authenticate(request.getUsername(),request.getPlaintextPassword());

    String jwt = authService.generateToken(user.getId(),user.getUsername());

    return ResponseEntity.ok(new LoginResponseDTO(user.getId(), user.getUsername(), jwt));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody UserRegisterDTO request){

    System.out.println("Auth in context: " + SecurityContextHolder.getContext().getAuthentication());

    User user = userMapper.mapUser(request);
    if(userService.doesEmailExist(user.getEmail())){
      return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ErrorResponseDTO.builder()
          .status("409")
          .error("conflict")
          .message("A user with this email already exists!")
          .timestamp(Timestamp.from(Instant.now())).build()
        );
    }

    try{
      User userFromDB = userService.getUserByUsername(user.getUsername());
    } catch (EntityNotFoundException e) {
      userService.registerUser(user);
      UserResponseDTO userResponseDTO = userMapper.mapUser(user);
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userResponseDTO);
    }

    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(ErrorResponseDTO.builder()
        .status("409")
        .error("conflict")
        .message("A user with this username already exists!").build()
      );

  }
}
