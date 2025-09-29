package com.bjjnotetaker.bjjnotetaker.dto.sessionManagement;

import com.bjjnotetaker.bjjnotetaker.domain.Session;
import lombok.Data;

import java.util.List;

@Deprecated
@Data
public class UserSessionsDTO {
  List<SessionDTO> sessions;
}
