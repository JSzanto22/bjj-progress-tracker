package com.bjjnotetaker.bjjnotetaker;

import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.repository.UserRepository;
import com.bjjnotetaker.bjjnotetaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bjjnotetaker.bjjnotetaker.repository.UserRepository;
@RestController
@RequestMapping("/users") // optional base path
public class testController {

  private final UserRepository userRepository;

  public testController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Autowired
  UserService userService;

  // Using path variable
  @GetMapping("/{username}")
  public User getUserByPath(@PathVariable String username) {
    User user = User.builder()
      .username(username)
      .email("email:" + System.currentTimeMillis() )
      .beltRank("blue")
      .stripeCount(0)
      .build();
    userService.registerUser(user);
    return userRepository.findByUsername(username);
  }


  // Using query parameter
  @GetMapping
  public User getUserByQuery(@RequestParam String username) {
    return userRepository.findByUsername(username);
  }
}
