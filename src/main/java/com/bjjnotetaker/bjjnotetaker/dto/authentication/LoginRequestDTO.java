package com.bjjnotetaker.bjjnotetaker.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Value;

@Data
public class LoginRequestDTO {
  @NotBlank
  private String username;
  @NotBlank
  private String plaintextPassword;
}
