package com.bjjnotetaker.bjjnotetaker.dto.sessionManagement;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Date;

@Data
public class SessionRequestDTO  {
  private long id;
  @NotEmpty
  private Date fromClassDate;
  //Null when requesting to create a new Session
  private Date toClassDate;
  private String classType;
  private Integer duration;
  private String notes;
}
