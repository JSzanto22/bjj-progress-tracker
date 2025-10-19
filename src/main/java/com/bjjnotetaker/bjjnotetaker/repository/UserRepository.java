package com.bjjnotetaker.bjjnotetaker.repository;

import com.bjjnotetaker.bjjnotetaker.domain.User;
import org.springframework.context.annotation.ReflectiveScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
  Boolean existsByEmail(String email);
}
