package com.example.Baja.Controller;


import com.example.Baja.Entity.AttendanceEntity;
import com.example.Baja.Entity.UserDetailsEntity;
import com.example.Baja.Repo.AttendanceRepo;
import com.example.Baja.Repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private UserDetailsRepo userDetailsRepo; // To retrieve user details

    // Endpoint to retrieve all members for attendance marking
    // This is useful for populating a list/table on the admin's UI
    @GetMapping("/members")
    public ResponseEntity<List<UserDetailsEntity>> getAllMembersForAttendance() {
        // You might want to filter or select specific fields here if the list is very large
        return ResponseEntity.ok(userDetailsRepo.findAll());
    }

    /**
     * Endpoint to mark attendance for multiple members on a specific date.
     * The request body will be a JSON object mapping user IDs to their presence status.
     * Example JSON Request Body:
     * {
     * "date": "2025-06-07",
     * "attendanceRecords": [
     * {"userId": 1, "present": true},
     * {"userId": 2, "present": false},
     * {"userId": 3, "present": true}
     * ]
     * }
     */
    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(@RequestBody AttendanceRequest attendanceRequest) {
        LocalDate date;
        try {
            date = LocalDate.parse(attendanceRequest.getDate());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use YYYY-MM-DD.");
        }

        // Before saving, you might want to delete existing attendance for this date
        // to prevent duplicates if the admin submits multiple times for the same day.
        // Or, you can check and update if an entry already exists.
        // For simplicity, this example will create/update.

        for (AttendanceRecord record : attendanceRequest.getAttendanceRecords()) {
            Optional<UserDetailsEntity> userOptional = userDetailsRepo.findById(record.getUserId());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User with ID " + record.getUserId() + " not found.");
            }
            UserDetailsEntity user = userOptional.get();

            // Check if attendance already exists for this user on this date
            Optional<AttendanceEntity> existingAttendance = attendanceRepo.findByUser_IdAndAttendanceDate(user.getId(), date);

            AttendanceEntity attendance;
            if (existingAttendance.isPresent()) {
                attendance = existingAttendance.get();
                attendance.setPresent(record.isPresent()); // Update status
            } else {
                attendance = new AttendanceEntity(user, date, record.isPresent());
            }
            attendanceRepo.save(attendance);
        }

        return ResponseEntity.ok("Attendance marked successfully for " + date);
    }

    /**
     * Endpoint to get attendance for a specific date (e.g., to review it).
     * Example: GET /api/attendance/date?date=2025-06-07
     */
    @GetMapping("/date")
    public ResponseEntity<List<AttendanceRecordResponse>> getAttendanceByDate(@RequestParam String date) {
        LocalDate attendanceDate;
        try {
            attendanceDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null); // Or return a specific error DTO
        }

        List<AttendanceEntity> records = attendanceRepo.findByAttendanceDate(attendanceDate);

        // Convert AttendanceEntity to a simpler DTO for response
        List<AttendanceRecordResponse> response = records.stream()
                .map(rec -> new AttendanceRecordResponse(
                        rec.getUser().getId(),
                        rec.getUser().getName(), // Assuming getName() exists in UserDetailsEntity
                        rec.isPresent()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}

// --- DTOs for Request and Response ---
// These are helper classes to structure your JSON payloads

// Request DTO for marking attendance
class AttendanceRequest {
    private String date; // YYYY-MM-DD format
    private List<AttendanceRecord> attendanceRecords;

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(List<AttendanceRecord> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }
}

// Sub-DTO for individual attendance record in the request
class AttendanceRecord {
    private Long userId;
    private boolean present;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}

// Response DTO for retrieving attendance
class AttendanceRecordResponse {
    private Long userId;
    private String userName;
    private boolean present;

    public AttendanceRecordResponse(Long userId, String userName, boolean present) {
        this.userId = userId;
        this.userName = userName;
        this.present = present;
    }

    // Getters (Setters usually not needed for response DTOs unless for deserialization)
    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isPresent() {
        return present;
    }
}