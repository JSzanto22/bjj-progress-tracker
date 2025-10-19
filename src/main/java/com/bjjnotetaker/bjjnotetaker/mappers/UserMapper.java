package com.bjjnotetaker.bjjnotetaker.mappers;


import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.dto.sessionManagement.SessionDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserRegisterDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserResponseDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserUpdateDTO;
import com.bjjnotetaker.bjjnotetaker.service.AuthService;
import com.bjjnotetaker.bjjnotetaker.service.PasswordHashingService;
import com.bjjnotetaker.bjjnotetaker.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

  @Autowired
  PasswordHashingService passwordHashingService;

  @Autowired
  AuthService authService;

  @Autowired
  SessionMapper sessionMapper;


  public User mapUser(final UserRegisterDTO userRegisterDTO) {
    final User user = User.builder()
      .username(userRegisterDTO.getUsername())
      .email(userRegisterDTO.getEmail())
      .passwordHash(passwordHashingService.hashPassword(userRegisterDTO.getPassword()))
      .beltRank(userRegisterDTO.getBeltRank())
      .stripeCount(userRegisterDTO.getStripeCount())
      .build();
    return user;
  }

  public UserResponseDTO mapUser(final User user) {
    List<SessionDTO> sessions = new ArrayList<>();
    if(user.getStripeCount() != null && user.getStripeCount() > 0) {
     sessions = sessionMapper.mapSessions(user.getSessions());
    }

    final UserResponseDTO userResponseDTO = UserResponseDTO.builder()
      .id(user.getId())
      .username(user.getUsername())
      .beltRank(user.getBeltRank())
      .joinDate(user.getJoinDate())
      .attendance(user.getAttendanceCount())
      .jwt(authService.generateToken(user.getId(), user.getUsername()))
      .build();
    userResponseDTO.setSessions(sessions);
    return userResponseDTO;
  }

  public User mapUser(UserUpdateDTO userUpdateDTO) {
    User user = User.builder()
      .email(userUpdateDTO.getEmail())
      .username(userUpdateDTO.getUsername())
      .beltRank(userUpdateDTO.getBeltRank())
      .stripeCount(userUpdateDTO.getStripeCount())
      .build();

      return user;
  }


}
