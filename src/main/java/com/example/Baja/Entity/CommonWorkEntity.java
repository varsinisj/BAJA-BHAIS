package com.example.Baja.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "common_work_entity") // Renamed table name for clarity
public class CommonWorkEntity { // Renamed from WorkEntity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate dueDate;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true) // Set to true if general work won't have a user
    private UserDetailsEntity user;


    // Constructors
    public CommonWorkEntity() { // Renamed constructor
    }

    public CommonWorkEntity(String title, String description, LocalDate dueDate) { // Renamed constructor
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public UserDetailsEntity getUser() {
        return user;
    }

    public void setUser(UserDetailsEntity user) {
        this.user = user;
    }
}