package com.bjjnotetaker.bjjnotetaker.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
  @NotBlank(message = "Username cannot be empty!")
  private String username;
  @NotBlank(message = "Password cannot be empty!")
  private String password;
}
