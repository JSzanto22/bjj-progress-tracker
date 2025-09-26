package com.bjjnotetaker.bjjnotetaker.repository;

import com.bjjnotetaker.bjjnotetaker.domain.Session;
import com.bjjnotetaker.bjjnotetaker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
  Session findByUser_id(Long id);
  List<Session> findAllByUser_id(Long id);

//  List<Session> getSessionsByClassDateBetween(User user, Date classDateAfter, Date classDateBefore);
}
