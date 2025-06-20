package com.example.Baja.Repo;

import com.example.Baja.Entity.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepo extends JpaRepository<WorkEntity, Long> {
    // Add this method declaration
    List<WorkEntity> findByUser_Id(Long userId);
}