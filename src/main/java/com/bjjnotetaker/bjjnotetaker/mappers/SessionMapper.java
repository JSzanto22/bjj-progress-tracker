package com.bjjnotetaker.bjjnotetaker.mappers;

import com.bjjnotetaker.bjjnotetaker.domain.Session;
import com.bjjnotetaker.bjjnotetaker.dto.sessionManagement.SessionDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SessionMapper {

  public List<SessionDTO> mapSessions(List<Session> sessions){

    List<SessionDTO> sessionDTOs = new ArrayList<>();

    for(Session x : sessions){
      sessionDTOs.add(mapSession(x));
    }

    return sessionDTOs;
  }

  private SessionDTO mapSession(Session session){
    return SessionDTO.builder()
      .id(session.getId())
      .notes(session.getNotes())
      .classType(session.getClassType())
      .classDate(session.getClassDate())
      .duration(session.getDuration())
      .build();
  }

}
