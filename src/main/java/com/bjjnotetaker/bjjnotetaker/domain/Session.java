package com.bjjnotetaker.bjjnotetaker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sessions")
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne() // removed "cascade = CascadeType.ALL"
  @JoinColumn(name = "user_id", nullable = false)
  @JsonBackReference //if session is retrieved via json, won't grab User. If it was retrieved will cause infinite recursion.
  private User user;

  @Column(name = "class_date")
  private Date classDate;

  @Column(name = "class_type")
  private String classType;
  private Integer duration;
  private String notes;

}
