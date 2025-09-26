package com.bjjnotetaker.bjjnotetaker.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String email;

  @Column(name = "belt_rank")
  private String beltRank;

  @Column(name = "stripe_count")
  private Integer stripeCount;

  @Column(name = "join_date")
  private Date joinDate;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Session> sessions = new ArrayList<>();

  @Transient
  public Integer getAttendanceCount() {
    return sessions != null ? sessions.size() : 0;
  }
}
