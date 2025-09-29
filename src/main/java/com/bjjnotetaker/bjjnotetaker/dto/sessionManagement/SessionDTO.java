package com.bjjnotetaker.bjjnotetaker.dto.sessionManagement;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Builder
@Data
public class SessionDTO {
  private Long id;
  private Date classDate;
  private String classType;
  private Integer duration;
  private String notes;
}
