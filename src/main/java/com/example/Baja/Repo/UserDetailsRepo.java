package com.example.Baja.Repo;

import com.example.Baja.Entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // <--- Import Optional

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetailsEntity, Long> {
    // Change this to return Optional<UserDetailsEntity>
    Optional<UserDetailsEntity> findByEmail(String email);
}