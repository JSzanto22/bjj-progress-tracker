package com.bjjnotetaker.bjjnotetaker.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDTO {
  private Long userId;
  private String username;
  private String jwt;
}
