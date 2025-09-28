package com.bjjnotetaker.bjjnotetaker.dto.sessionManagement;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Date;

@Data
public class SessionRequestDTO  {
  @NotEmpty
  private Date classDate;
  private String classType;
  private Integer duration;
  private String notes;
}
