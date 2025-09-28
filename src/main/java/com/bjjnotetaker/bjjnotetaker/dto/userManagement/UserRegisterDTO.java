package com.bjjnotetaker.bjjnotetaker.dto.userManagement;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDTO {
  @Email
  @NotBlank
 private String username;
 private String email;
  @NotBlank
 private String password;
 private String beltRank;
 private Integer stripeCount;
}
