package com.bjjnotetaker.bjjnotetaker.controllers;

import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserResponseDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserUpdateDTO;
import com.bjjnotetaker.bjjnotetaker.mappers.UserMapper;
import com.bjjnotetaker.bjjnotetaker.service.AuthService;
import com.bjjnotetaker.bjjnotetaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  UserMapper userMapper;
  @Autowired
  private UserService userService;

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

}
