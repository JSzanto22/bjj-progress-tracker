package com.bjjnotetaker.bjjnotetaker.service;

import com.bjjnotetaker.bjjnotetaker.domain.Session;
import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class SessionService {

  private final SessionRepository sessionRepository;
  private final UserService userService;

  public SessionService(SessionRepository sessionRepository, UserService userService) {
    this.sessionRepository = sessionRepository;
    this.userService = userService;
  }

  public Session createSessionForUser(String username, Session sessionData) {
    User user = userService.getUserByUsername(username);
    sessionData.setUser(user);
    return sessionRepository.save(sessionData);
  }

  public void deleteSessionForUser(Session sessionData, String username) {
    sessionData.setUser(userService.getUserByUsername(username));
    sessionRepository.delete(sessionData);
  }

  public void updateSessionForUser(String username, Session sessionData) {
    User user = userService.getUserByUsername(username);
    sessionData.setUser(user);
    sessionRepository.save(sessionData);
  }

  public List<Session> getAllSessionsForUser(String username) {
    User user = userService.getUserByUsername(username);
    return sessionRepository.findAllByUser_id(user.getId());
  }

  public List<Session> getSessionsForUserBetweenDates(String username, Date startDate, Date endDate) {
    User user = userService.getUserByUsername(username);
    return sessionRepository.getSessionsByClassDateBetween(user, startDate, endDate);
  }

  public void deleteSessionsForUser(String username, List<Long> sessionIds) {
    User user = userService.getUserByUsername(username);
    sessionRepository.deleteSessionByIdInAndUserId(sessionIds, user.getId());
  }

  public Session getSessionsForUserAndId(Long sessionId, Long userId) {
    return sessionRepository.getSessionByIdAndUserId(sessionId, userId);
  }

}
