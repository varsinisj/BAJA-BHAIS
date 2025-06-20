
package com.example.Baja.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "online_class_links")
public class OnlineClassLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated ID, though we'll manage conceptually a single record

    @Column(nullable = false, length = 500) // Meet link URL
    private String meetLink;

    @Column(columnDefinition = "TEXT") // Topic or description for the class
    private String topic;

    @Column(nullable = false)
    private LocalDateTime lastUpdated; // Timestamp of last update

    // Constructors
    public OnlineClassLinkEntity() {
        this.lastUpdated = LocalDateTime.now();
    }

    public OnlineClassLinkEntity(String meetLink, String topic) {
        this.meetLink = meetLink;
        this.topic = topic;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeetLink() {
        return meetLink;
    }

    public void setMeetLink(String meetLink) {
        this.meetLink = meetLink;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}