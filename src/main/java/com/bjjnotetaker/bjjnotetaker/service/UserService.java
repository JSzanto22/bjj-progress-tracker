package com.bjjnotetaker.bjjnotetaker.service;

import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {this.userRepository = userRepository;}

  public User registerUser(User user){
    return userRepository.save(user);
  }

  public void deleteUser(User user){
    userRepository.delete(user);
  }

  public User getUserByUsername(String username){
    User user = userRepository.findByUsername(username);
//    if(user == null){throw new EntityNotFoundException("User not found with username: " + username);}

    return user;
  }

  public User updateUser(User user){
    return userRepository.save(user);
  }
}
