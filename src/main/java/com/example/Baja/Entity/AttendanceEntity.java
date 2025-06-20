package com.example.Baja.Entity;


import jakarta.persistence.*;
import java.time.LocalDate; // Use LocalDate for date without time

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "attendance")
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne relationship with UserDetailsEntity
    // A user can have many attendance records
    @JsonBackReference // <-- This is correct
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading is good for performance
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column
    private UserDetailsEntity user; // The member whose attendance is being marked

    @Column(nullable = false)
    private LocalDate attendanceDate; // The date for which attendance is marked

    @Column(nullable = false)
    private boolean present; // true for present, false for absent

    // You could add more fields if needed, e.g.,
    // private String markedBy; // Who marked the attendance (e.g., admin's ID)
    // private LocalDateTime markedAt; // When it was marked

    // Constructors
    public AttendanceEntity() {
    }

    public AttendanceEntity(UserDetailsEntity user, LocalDate attendanceDate, boolean present) {
        this.user = user;
        this.attendanceDate = attendanceDate;
        this.present = present;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDetailsEntity getUser() {
        return user;
    }

    public void setUser(UserDetailsEntity user) {
        this.user = user;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}