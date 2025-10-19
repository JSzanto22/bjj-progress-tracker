package com.bjjnotetaker.bjjnotetaker.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class ErrorResponseDTO {
  private String status;
  private String error;
  private String message;
  private Timestamp timestamp;
}
