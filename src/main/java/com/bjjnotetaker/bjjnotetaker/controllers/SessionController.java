package com.bjjnotetaker.bjjnotetaker.controllers;

import com.bjjnotetaker.bjjnotetaker.domain.Session;
import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.dto.sessionManagement.SessionDTO;
import com.bjjnotetaker.bjjnotetaker.dto.sessionManagement.SessionRequestDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserResponseDTO;
import com.bjjnotetaker.bjjnotetaker.dto.userManagement.UserUpdateDTO;
import com.bjjnotetaker.bjjnotetaker.mappers.SessionMapper;
import com.bjjnotetaker.bjjnotetaker.mappers.UserMapper;
import com.bjjnotetaker.bjjnotetaker.service.AuthService;
import com.bjjnotetaker.bjjnotetaker.service.SessionService;
import com.bjjnotetaker.bjjnotetaker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

  @Autowired
  private SessionService sessionService;
  @Autowired
  private SessionMapper sessionMapper;

  @GetMapping("/all")
  public ResponseEntity<List<SessionDTO>> getAllSessions(@AuthenticationPrincipal User principal) {
    List<Session> sessions;
    try {
      sessions = sessionService.getAllSessionsForUser(principal.getUsername());
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
    List<SessionDTO> mySessionDTOS = sessionMapper.mapSessions(sessions);

    return ResponseEntity.ok(mySessionDTOS);
  }

  @GetMapping("/range")
  public ResponseEntity<List<SessionDTO>> getAllSessions(@AuthenticationPrincipal User principal, @RequestBody SessionRequestDTO sessionRequestDTO) {
    List<SessionDTO> mySessionDTOS;
    try {
      List<Session> sessions = sessionService.getSessionsForUserBetweenDates(principal.getUsername(), sessionRequestDTO.getFromClassDate(), sessionRequestDTO.getToClassDate());
      mySessionDTOS = sessionMapper.mapSessions(sessions);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(mySessionDTOS);
  }

  @PostMapping("/create")
  public ResponseEntity<Void> createOneSession(@AuthenticationPrincipal User principal, @RequestBody SessionRequestDTO sessionRequestDTO){
    Session session = sessionMapper.mapSession(sessionRequestDTO);

    sessionService.createSessionForUser(principal.getUsername(), session);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteSessions(@AuthenticationPrincipal User principal, List<Long> sessionIds){
    sessionService.deleteSessionsForUser(principal.getUsername(), sessionIds);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/update")
  public ResponseEntity<Void> updateSession(@AuthenticationPrincipal User principal, @RequestBody SessionRequestDTO sessionRequestDTO){
    Session sessionFromDB = sessionService.getSessionsForUserAndId(sessionRequestDTO.getId(), principal.getId());

    sessionFromDB.builder()
      .notes(sessionRequestDTO.getNotes())
      .classType(sessionRequestDTO.getClassType())
      .duration(sessionRequestDTO.getDuration())
      .classDate(sessionRequestDTO.getFromClassDate()).build();

    sessionService.updateSessionForUser(principal.getUsername(), sessionFromDB);

    return ResponseEntity.noContent().build();
  }
}
