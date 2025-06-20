package com.example.Baja.Controller;

import com.example.Baja.Entity.FeedbackEntity;
import com.example.Baja.Service.FeedbackService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors; // For stream operations

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // DTO for incoming feedback data (Optional, but good practice)
    static class FeedbackRequest {
        private String feedbackText;

        public String getFeedbackText() {
            return feedbackText;
        }

        public void setFeedbackText(String feedbackText) {
            this.feedbackText = feedbackText;
        }
    }

    // DTO for outgoing feedback data (to include user email)
    // This prevents sending the entire UserDetailsEntity object which might have sensitive info
    static class FeedbackResponseDTO {
        private Long id;
        private String userEmail; // Changed from 'email' to 'userEmail' for clarity
        private String feedbackText;
        private LocalDateTime submissionDate;

        // Constructor to convert FeedbackEntity to DTO
        public FeedbackResponseDTO(FeedbackEntity feedbackEntity) {
            this.id = feedbackEntity.getId();
            this.userEmail = feedbackEntity.getUser() != null ? feedbackEntity.getUser().getEmail() : "N/A";
            this.feedbackText = feedbackEntity.getFeedbackText();
            this.submissionDate = feedbackEntity.getSubmissionDate();
        }

        // Getters for JSON serialization
        public Long getId() { return id; }
        public String getUserEmail() { return userEmail; }
        public String getFeedbackText() { return feedbackText; }
        public LocalDateTime getSubmissionDate() { return submissionDate; }

        // Setters (optional, not strictly needed for outgoing DTO)
        public void setId(Long id) { this.id = id; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
        public void setFeedbackText(String feedbackText) { this.feedbackText = feedbackText; }
        public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }
    }


    @PostMapping("/submit")
    public ResponseEntity<?> submitFeedback(@RequestBody FeedbackRequest feedbackRequest, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "User not logged in or session expired."));
        }

        if (feedbackRequest.getFeedbackText() == null || feedbackRequest.getFeedbackText().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("message", "Feedback text cannot be empty."));
        }

        try {
            FeedbackEntity savedFeedback = feedbackService.saveFeedback(userId, feedbackRequest.getFeedbackText());
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(Map.of("message", "Feedback submitted successfully!", "feedbackId", savedFeedback.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("message", "Error submitting feedback: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllFeedback() {
        try {
            List<FeedbackEntity> feedbackList = feedbackService.findAllFeedback();

            // Convert FeedbackEntity list to FeedbackResponseDTO list
            List<FeedbackResponseDTO> feedbackDTOList = feedbackList.stream()
                .map(FeedbackResponseDTO::new) // Uses the DTO constructor
                .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", feedbackDTOList.isEmpty() ? "No feedback found." : "Feedback retrieved successfully.");
            response.put("data", feedbackDTOList); // Return the DTO list
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error retrieving feedback: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}