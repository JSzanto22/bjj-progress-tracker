package com.bjjnotetaker.bjjnotetaker;

import com.bjjnotetaker.bjjnotetaker.domain.Session;
import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.dto.authentication.LoginResponseDTO;
import com.bjjnotetaker.bjjnotetaker.service.AuthService;
import com.bjjnotetaker.bjjnotetaker.service.PasswordHashingService;
import com.bjjnotetaker.bjjnotetaker.service.SessionService;
import com.bjjnotetaker.bjjnotetaker.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class BjjnotetakerApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private SessionService sessionService;

  @Autowired
  private UserService userService;

  @Autowired
  PasswordHashingService passwordHashingService;

  @Autowired
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void contextLoads() {
  }

  @Test
  void testThatRegisterUserWorks() {
    User user = User.builder()
      .username("JUNITuser")
      .email("Junitemail@email.com")
      .beltRank("blue")
      .stripeCount(0)
      .passwordHash(passwordHashingService.hashPassword("Password123"))
      .build();

    userService.registerUser(user);
    User sameUser = userService.getUserByUsername("JUNITuser");
    user.setBeltRank("green");
    assertNotNull(sameUser);
    assertEquals("JUNITuser", sameUser.getUsername());
  }

  @Test
  void testThatDeleteUserWorks() {
    User user = User.builder()
      .username("JUNITuser")
      .email("Junitemail@email.com")
      .beltRank("blue")
      .stripeCount(0)
      .passwordHash(passwordHashingService.hashPassword("Password123"))
      .build();

    userService.registerUser(user);
    assertNotNull(userService.getUserByUsername("JUNITuser")); //check if user was registered

    userService.deleteUser(user);
    assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername("JUNITuser")); //check if user was removed
  }

  @Test
  void testThatUpdateUserWorks() {
    User user = User.builder()
      .username("JUNITuser")
      .email("Junitemail@email.com")
      .beltRank("blue")
      .stripeCount(0)
      .passwordHash(passwordHashingService.hashPassword("Password123"))
      .build();

    userService.registerUser(user);

    user.setUsername("NotJunitUser"); user.setBeltRank("red"); user.setEmail("DifferentEmail@email.com");
    userService.updateUser(user);

    assertEquals("NotJunitUser", userService.getUserByUsername("NotJunitUser").getUsername());
    assertEquals("red", userService.getUserByUsername("NotJunitUser").getBeltRank());
    assertEquals("DifferentEmail@email.com", userService.getUserByUsername("NotJunitUser").getEmail());

    assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername("JUNITuser")); //Check that old registration is gone
  }

  @Test
  void testThatCreateSessionWorks() {
    User user = User.builder()
      .username("JUNITuser")
      .email("Junitemail@email.com")
      .beltRank("blue")
      .stripeCount(0)
      .passwordHash(passwordHashingService.hashPassword("Password123"))
      .build();
    userService.registerUser(user);

    Session session = Session.builder()
      .classDate(new Date(Date.valueOf("2025-09-23").getTime()))
      .classType("GI")
      .duration(233)
      .notes("Worked on leglocks")
      .build();
    sessionService.createSessionForUser("JUNITuser", session);

    assertNotNull(sessionService.getAllSessionsForUser("JUNITuser"));

    List<Session> sessions = sessionService.getAllSessionsForUser("JUNITuser");
    assert(sessions.size() == 1);
    assert(sessions.get(0).getClassType().equals("GI")
      && sessions.get(0).getDuration() == 233
      && sessions.get(0).getNotes().equals("Worked on leglocks")
      && sessions.get(0).getClassDate().equals(Date.valueOf("2025-09-23"))
    );

  }

  @Test
  void testThatDeleteSessionWorks() {
    User user = User.builder()
      .username("JUNITuser")
      .email("Junitemail@email.com")
      .beltRank("blue")
      .stripeCount(0)
      .passwordHash(passwordHashingService.hashPassword("Password123"))
      .build();
    userService.registerUser(user);

    Session session = Session.builder()
      .classDate(new Date(Date.valueOf("2025-09-23").getTime()))
      .classType("GI")
      .duration(233)
      .notes("Worked on leglocks")
      .build();
    sessionService.createSessionForUser("JUNITuser", session);

    assertNotNull(sessionService.getAllSessionsForUser("JUNITuser"));
    List<Session> sessions = sessionService.getAllSessionsForUser("JUNITuser");
    assert(sessions.size() == 1);

    sessionService.deleteSessionForUser(session, "JUNITuser");

    assertNotNull(userService.getUserByUsername("JUNITuser"));
    assert(sessionService.getAllSessionsForUser("JUNITuser").isEmpty());

  }

  @Test
  void testThatUpdateSessionWorks() {
    User user = User.builder()
      .username("JUNITuser")
      .email("Junitemail@email.com")
      .beltRank("blue")
      .stripeCount(0)
      .passwordHash(passwordHashingService.hashPassword("Password123"))
      .build();
    userService.registerUser(user);

    Session session = Session.builder()
      .classDate(new Date(Date.valueOf("2025-09-23").getTime()))
      .classType("GI")
      .duration(233)
      .notes("Worked on leglocks")
      .build();
    sessionService.createSessionForUser("JUNITuser", session);
//    System.out.println("Pre-update: " + sessionService.getAllSessionsForUser("JUNITuser").get(0));

    session.setNotes("updated notes");
    session.setClassType("NO-GI");

    sessionService.updateSessionForUser("JUNITuser", session);

    List<Session> newSessions = sessionService.getAllSessionsForUser("JUNITuser");
    assert(newSessions.size() == 1);
    assertEquals("updated notes", newSessions.get(0).getNotes()); //Altered
    assertEquals("NO-GI", newSessions.get(0).getClassType());  //Altered
    assertEquals(233, newSessions.get(0).getDuration()); //Same
    assertEquals("2025-09-23", newSessions.get(0).getClassDate().toString()); //Same

//    System.out.println("Post-update: " + newSessions.get(0));
  }

  @Test
  void testThatGetAllSessionsBetweenDatesWorks() {
    User user = User.builder()
      .username("JUNITuser")
      .email("Junitemail@email.com")
      .beltRank("blue")
      .stripeCount(0)
      .passwordHash(passwordHashingService.hashPassword("Password123"))
      .build();
    userService.registerUser(user);

    Session session1 = Session.builder()
      .classDate(new Date(Date.valueOf("2025-09-23").getTime()))
      .classType("GI")
      .duration(233)
      .notes("Worked on leglocks")
      .build();
    sessionService.createSessionForUser("JUNITuser", session1);

    Session session2 = Session.builder()
      .classDate(new Date(Date.valueOf("2025-11-23").getTime()))
      .classType("GI")
      .duration(233)
      .notes("Worked on leglocks")
      .build();
    sessionService.createSessionForUser("JUNITuser", session2);

    Session session3 = Session.builder()
      .classDate(new Date(Date.valueOf("2024-08-23").getTime()))
      .classType("GI")
      .duration(233)
      .notes("Worked on leglocks")
      .build();
    sessionService.createSessionForUser("JUNITuser", session3);

    Date date1 = new Date(Date.valueOf("2024-09-23").getTime());
    Date date2 = new Date(Date.valueOf("2025-11-23").getTime());

    List<Session> sessions = sessionService.getSessionsForUserBetweenDates("JUNITuser", date1, date2);

    assert(sessions.size() == 2);
  }

  @Test
  void testThatPasswordHashWorks() {

    String password = "password";

    String hashedPassword = passwordHashingService.hashPassword(password);

    System.out.println(hashedPassword);
    assert(!hashedPassword.equals(password));

  }

  @Test
  void testThatPasswordMatchesWorks() {
    PasswordHashingService passwordHashingService = new PasswordHashingService();

    String password = "password";
    String hashedPassword = passwordHashingService.hashPassword(password);

    assertNotEquals(password, hashedPassword);
    assert(passwordHashingService.passwordsMatch(password, hashedPassword));
  }

  @Test
  void testAuthServiceWorks() {
    User user = User.builder()
      .username("JUNITuser")
      .email("Junitemail@email.com")
      .beltRank("blue")
      .stripeCount(0)
      .passwordHash(passwordHashingService.hashPassword("Password123"))
      .build();
    userService.registerUser(user);

    User registeredUser = userService.getUserByUsername("JUNITuser");
    String token = authService.generateToken(registeredUser.getId(), registeredUser.getUsername());

    assertTrue(authService.validateToken(token));
    assertEquals(registeredUser.getUsername(), authService.getUsernameFromToken(token));
    assertEquals(registeredUser.getId(), authService.getUserIdFromToken(token));
    //  System.out.println(token);
  }

  @Test
  void testAuthControllerRegisterWorks() throws Exception {
    mockMvc.perform(post("/api/auth/register")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {"username":"myame98", "email":"myEmail@gmail.com","password":"randomPassoword123","beltRank":"blue","stripeCount":0}
        """))
      .andExpect(status().isCreated());
}

@Test
  void testAuthControllerLoginWorks() throws Exception {
  mockMvc.perform(post("/api/auth/register")
    .contentType(MediaType.APPLICATION_JSON)
    .content("""
        {"username":"myame98", "email":"myEmail@gmail.com","password":"randomPassoword123","beltRank":"blue","stripeCount":0}
        """)).andExpect(status().isCreated());

  MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
    .contentType(MediaType.APPLICATION_JSON)
    .content("""
        {"username":"myame98","password":"randomPassoword123"}
        """)).andExpect(status().isOk()).andReturn();

  String jwt = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LoginResponseDTO.class).getJwt();

  assertTrue(authService.validateToken(jwt));
}

}

