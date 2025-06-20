package com.example.Baja.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback") // Name of your feedback table in the database
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One relationship with UserDetailsEntity
    // A user can submit many feedback entries
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to UserDetailsEntity
    private UserDetailsEntity user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feedbackText; // The actual feedback message

    private LocalDateTime submissionDate; // Timestamp of when feedback was submitted

    // Constructors
    public FeedbackEntity() {
        this.submissionDate = LocalDateTime.now(); // Set current time on creation
    }

    public FeedbackEntity(UserDetailsEntity user, String feedbackText) {
        this.user = user;
        this.feedbackText = feedbackText;
        this.submissionDate = LocalDateTime.now();
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

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
}