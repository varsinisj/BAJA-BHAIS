package com.example.Baja.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SignupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    private String resetToken;
    private LocalDateTime tokenExpiry;
    @OneToOne(mappedBy = "signup", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserDetailsEntity userDetails;
    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

   
}
