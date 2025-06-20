package com.example.Baja.Repo;

import com.example.Baja.Entity.CommonWorkEntity; // Import renamed entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonWorkRepo extends JpaRepository<CommonWorkEntity, Long> { // Use renamed entity

}