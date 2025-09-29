package com.bjjnotetaker.bjjnotetaker.dto.userManagement;

import com.bjjnotetaker.bjjnotetaker.dto.sessionManagement.SessionDTO;
import com.bjjnotetaker.bjjnotetaker.dto.sessionManagement.UserSessionsDTO;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Builder
@Data
public class UserResponseDTO {
 private Long id;
 private String username;
 private String beltRank;
 private Date joinDate;
 private List<SessionDTO> sessions;
 private Integer attendance;
 private String jwt;
}
