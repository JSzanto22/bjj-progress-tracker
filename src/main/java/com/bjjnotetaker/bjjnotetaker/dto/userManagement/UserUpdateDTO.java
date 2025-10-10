package com.bjjnotetaker.bjjnotetaker.dto.userManagement;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDTO {
  @Email
  String email;
  String username;
  String beltRank;
  Integer stripeCount;
}
