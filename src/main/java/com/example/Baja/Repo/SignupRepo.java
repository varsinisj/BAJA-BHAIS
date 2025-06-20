package com.example.Baja.Repo;



import com.example.Baja.Entity.SignupEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository

public interface SignupRepo extends JpaRepository<SignupEntity, Long> {

SignupEntity findByEmail(String email);


SignupEntity findByResetToken(String resetToken);
}