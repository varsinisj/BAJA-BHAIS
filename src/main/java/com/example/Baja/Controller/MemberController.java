package com.example.Baja.Controller;

import com.example.Baja.Service.WorkManagementService; // Import the service you just created
import com.example.Baja.Service.CombinedWorkResponse;   // Import the DTO
import com.example.Baja.Service.OnlineClassLinkService;

import jakarta.servlet.http.HttpSession; // To retrieve userId from session
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Baja.Service.AttendanceService; // Import the new AttendanceService
import com.example.Baja.Entity.AttendanceEntity; // Import AttendanceEntity
import com.example.Baja.Entity.OnlineClassLinkEntity;

import java.util.Map;
import java.util.*;
@RestController // Indicates this is a REST controller, automatically serializes return values to JSON
@RequestMapping("/api/member") // Base path for all member-specific API endpoints
public class MemberController {

    @Autowired
    private WorkManagementService workManagementService; // Inject your service layer
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private OnlineClassLinkService onlineClassLinkService;
    /**
     * Endpoint for members to retrieve their assigned individual work and all common work.
     * This method retrieves the logged-in user's ID from the HttpSession, ensuring security
     * by preventing users from querying other users' work by simply changing URL parameters.
     *
     * GET /api/member/my-work
     * @param session The current HttpSession, provided by Spring.
     * @return ResponseEntity containing CombinedWorkResponse with work data, or an error message.
     */
    @GetMapping("/my-work")
    public ResponseEntity<?> getMyWork(HttpSession session) {
        // Retrieve userId from the session. This ID was stored during successful login.
        Long userId = (Long) session.getAttribute("userId");

        // If userId is null, it means the user is not logged in, or their session has expired.
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
                                 .body(Map.of("message", "User not logged in or session expired."));
        }

        try {
            // Call the service layer to get the combined work data for the specific user
            CombinedWorkResponse combinedWork = workManagementService.getWorkForUser(userId);
            // Return the DTO with the work lists as a successful JSON response
            return ResponseEntity.ok(combinedWork); // 200 OK
        } catch (IllegalArgumentException e) {
            // This might happen if the userId somehow doesn't correspond to a valid user
            // or other business logic validation in the service.
            return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404 Not Found
                                 .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Catch any other unexpected errors during the process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                                 .body(Map.of("message", "Error retrieving work: " + e.getMessage()));
        }
    }
    /**
     * New Endpoint: Get attendance details for the logged-in user.
     * GET /api/member/my-attendance
     * @param session The current HttpSession to get userId.
     * @return ResponseEntity with a list of AttendanceEntity or an error message.
     */
    @GetMapping("/my-attendance")
    public ResponseEntity<?> getMyAttendance(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
                                 .body(Map.of("message", "User not logged in or session expired."));
        }

        try {
            List<AttendanceEntity> attendanceRecords = attendanceService.getUserAttendance(userId);
            return ResponseEntity.ok(attendanceRecords); // 200 OK, returns list of attendance records
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                                 .body(Map.of("message", "Error retrieving attendance: " + e.getMessage()));
        }
    }

    /**
     * New Endpoint: Get the online class link.
     * GET /api/member/online-class-link
     * @return ResponseEntity with the class link URL or an error message.
     */
    @GetMapping("/online-class-link")
    public ResponseEntity<?> getOnlineClassLink(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId"); // Check if user is logged in

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "User not logged in or session expired."));
        }

        try {
            Optional<OnlineClassLinkEntity> classLinkOptional = onlineClassLinkService.getCurrentClassLink();

            if (classLinkOptional.isPresent()) {
                // IMPORTANT: Use .getMeetLink() because your entity has 'meetLink' field
                // This will result in JSON like {"meetLink": "your_url"}
                return ResponseEntity.ok(Map.of("meetLink", classLinkOptional.get().getMeetLink()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(Map.of("message", "No online class link found."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("message", "Error retrieving class link: " + e.getMessage()));
        }
    }
  
}