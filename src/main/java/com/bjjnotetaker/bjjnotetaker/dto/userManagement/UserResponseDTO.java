package com.bjjnotetaker.bjjnotetaker.dto.userManagement;

import com.bjjnotetaker.bjjnotetaker.dto.sessionManagement.UserSessionsDTO;

import java.sql.Date;
import java.util.List;

public class UserResponseDTO {
 private Long id;
 private String username;
 private String beltRank;
 private Date joinDate;
 private List<UserSessionsDTO> sessions;
 private Integer attendance;
}
