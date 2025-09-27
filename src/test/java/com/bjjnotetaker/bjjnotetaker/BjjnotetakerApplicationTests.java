package com.bjjnotetaker.bjjnotetaker;

import com.bjjnotetaker.bjjnotetaker.domain.Session;
import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.service.SessionService;
import com.bjjnotetaker.bjjnotetaker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.List;


@SpringBootTest
@Transactional
class BjjnotetakerApplicationTests {

  @Autowired
  private SessionService sessionService;

  @Autowired
  private UserService userService;



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

    List<Session> sesions = sessionService.getSessionsForUserBetweenDates("JUNITuser", date1, date2);

    assert(sesions.size() == 2);
  }

}
