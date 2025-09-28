package com.bjjnotetaker.bjjnotetaker.dto.userManagement;

import jakarta.validation.constraints.Email;

public class UserUpdateDTO {
  @Email
  String email;
  String username;
  String password;
  String beltRank;
  Integer stripeCount;
}
