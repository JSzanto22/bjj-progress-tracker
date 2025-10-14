package com.bjjnotetaker.bjjnotetaker.controllers;

import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.PasswordUpdateDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserResponseDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserUpdateDTO;
import com.bjjnotetaker.bjjnotetaker.mappers.UserMapper;
import com.bjjnotetaker.bjjnotetaker.service.AuthService;
import com.bjjnotetaker.bjjnotetaker.service.PasswordHashingService;
import com.bjjnotetaker.bjjnotetaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserService userService;
  @Autowired
  private PasswordHashingService passwordHashingService;

  @GetMapping("/me")
  public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal User user) {
    UserResponseDTO userResponseDTO = userMapper.mapUser(user);

    return ResponseEntity.ok(userResponseDTO);
  }

  @PutMapping("/me")
  public ResponseEntity<UserResponseDTO> updateProfile(@AuthenticationPrincipal User principal, @RequestBody UserUpdateDTO userUpdateDTO) {
      User user = userService.getUserByUsername(principal.getUsername());

      user.setEmail(userUpdateDTO.getEmail());
      user.setUsername(userUpdateDTO.getUsername());
      user.setBeltRank(userUpdateDTO.getBeltRank());
      user.setStripeCount(userUpdateDTO.getStripeCount());

    userService.updateUser(user);

    return ResponseEntity.ok(userMapper.mapUser(user));
  }

  @PutMapping("/me/password")
  public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal User principal, @RequestBody PasswordUpdateDTO request) {
    User userFromDB = userService.getUserByUsername(principal.getUsername());

    if(!passwordHashingService.passwordsMatch(request.getOldPassword(), userFromDB.getPasswordHash())){
      return ResponseEntity.badRequest().build();
    }

    userFromDB.setPasswordHash(passwordHashingService.hashPassword(request.getNewPassword()));

    userService.updateUser(userFromDB);

    return ResponseEntity.noContent().build();
  }

}
