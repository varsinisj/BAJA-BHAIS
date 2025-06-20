package com.example.Baja.Controller;

import com.example.Baja.Service.AttendanceService; // Import the service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api") // Keep this base path for consistency if other /api endpoints exist
public class AttendanceSummaryController { // Renamed to AttendanceSummaryController

    @Autowired
    private AttendanceService attendanceService;

    // This is the endpoint for the "View Attendance" (all students, percentages) feature
    @GetMapping("/attendance/all-summary")
    public ResponseEntity<Map<String, Object>> getAllAttendanceSummary() {
        try {
            // This calls the method in AttendanceService that calculates percentages
            List<AttendanceService.AttendanceSummaryDTO> attendanceSummaryList = attendanceService.getDetailedAttendanceSummary();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", attendanceSummaryList.isEmpty() ? "No attendance data found." : "Attendance summary retrieved successfully.");
            response.put("data", attendanceSummaryList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error retrieving attendance summary: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}