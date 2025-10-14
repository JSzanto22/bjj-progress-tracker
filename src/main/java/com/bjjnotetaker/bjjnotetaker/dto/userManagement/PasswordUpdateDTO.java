package com.bjjnotetaker.bjjnotetaker.dto.userManagement;

import lombok.Data;

@Data
public class PasswordUpdateDTO {
  private String oldPassword;
  private String newPassword;
}
