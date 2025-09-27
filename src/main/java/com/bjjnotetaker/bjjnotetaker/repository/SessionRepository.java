package com.bjjnotetaker.bjjnotetaker.repository;

import com.bjjnotetaker.bjjnotetaker.domain.Session;
import com.bjjnotetaker.bjjnotetaker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
  Session findByUser_id(Long id);
  List<Session> findAllByUser_id(Long id);

  @Query("SELECT s FROM Session s WHERE s.user = :user AND s.classDate BETWEEN :classDateAfter AND :classDateBefore")
  List<Session> getSessionsByClassDateBetween(
    @Param("user") User user,
    @Param("classDateAfter") Date classDateAfter,
    @Param("classDateBefore") Date classDateBefore);
}
