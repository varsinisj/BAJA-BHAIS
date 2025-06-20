package com.example.Baja.Repo;

import com.example.Baja.Entity.AttendanceEntity;
// import com.example.Baja.Entity.UserDetailsEntity; // No direct import needed here if using user_Id

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceEntity, Long> {

    // Custom query to find attendance for a specific user on a specific date
    Optional<AttendanceEntity> findByUser_IdAndAttendanceDate(Long userId, LocalDate date);

    // Custom query to find all attendance records for a specific date
    List<AttendanceEntity> findByAttendanceDate(LocalDate date);

    // Custom query to find all attendance records for a specific user by their ID
    List<AttendanceEntity> findByUser_Id(Long userId);
}