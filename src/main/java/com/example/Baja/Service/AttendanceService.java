package com.example.Baja.Service;

import com.example.Baja.Entity.AttendanceEntity;
import com.example.Baja.Entity.UserDetailsEntity; // Needed to fetch all users
import com.example.Baja.Repo.AttendanceRepo;
import com.example.Baja.Repo.UserDetailsRepo; // Needed to fetch all users
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private UserDetailsRepo userDetailsRepo; // Inject UserDetailsRepo to get all users

    /**
     * Retrieves all attendance records for a specific user.
     * @param userId The ID of the user whose attendance records are to be retrieved.
     * @return A list of AttendanceEntity objects for the given user.
     */
    public List<AttendanceEntity> getUserAttendance(Long userId) {
        return attendanceRepo.findByUser_Id(userId);
    }

    /**
     * Calculates and returns a detailed attendance summary for all students,
     * including total classes, classes attended, and percentage.
     * @return A list of AttendanceSummaryDTO objects.
     */
    public List<AttendanceSummaryDTO> getDetailedAttendanceSummary() {
        List<UserDetailsEntity> allUsers = userDetailsRepo.findAll(); // Fetch all registered users

        return allUsers.stream().map(user -> {
            // Get all attendance records for the current user using the repo method
            List<AttendanceEntity> userAttendance = attendanceRepo.findByUser_Id(user.getId());

            long totalDays = userAttendance.size();
            long presentDays = userAttendance.stream()
                                             .filter(AttendanceEntity::isPresent) // Check the boolean 'present' field
                                             .count();

            double percentage = (totalDays > 0) ? (double) presentDays / totalDays * 100 : 0.0;

            return new AttendanceSummaryDTO(
                user.getId(),
                user.getName(), // Assuming getName() exists in UserDetailsEntity
                user.getEmail(),
                totalDays,
                presentDays,
                percentage
            );
        }).collect(Collectors.toList());
    }

    /**
     * DTO (Data Transfer Object) for attendance summary.
     * This helps in structuring the response sent back to the frontend.
     */
    public static class AttendanceSummaryDTO {
        private Long userId;
        private String userName;
        private String userEmail;
        private long totalClasses;
        private long classesAttended;
        private double attendancePercentage;

        // Constructor
        public AttendanceSummaryDTO(Long userId, String userName, String userEmail,
                                    long totalClasses, long classesAttended, double attendancePercentage) {
            this.userId = userId;
            this.userName = userName;
            this.userEmail = userEmail;
            this.totalClasses = totalClasses;
            this.classesAttended = classesAttended;
            this.attendancePercentage = attendancePercentage;
        }

        // Getters for JSON serialization
        public Long getUserId() { return userId; }
        public String getUserName() { return userName; }
        public String getUserEmail() { return userEmail; }
        public long getTotalClasses() { return totalClasses; }
        public long getClassesAttended() { return classesAttended; }
        public double getAttendancePercentage() { return attendancePercentage; }

        // Setters (optional, typically not needed for outgoing DTOs)
        public void setUserId(Long userId) { this.userId = userId; }
        public void setUserName(String userName) { this.userName = userName; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
        public void setTotalClasses(long totalClasses) { this.totalClasses = totalClasses; }
        public void setClassesAttended(long classesAttended) { this.classesAttended = classesAttended; }
        public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }
    }
}