package com.example.Baja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Baja.Entity.UserDetailsEntity;
import com.example.Baja.Entity.CommonWorkEntity; // Import renamed entity
import com.example.Baja.Repo.UserDetailsRepo;
import com.example.Baja.Repo.CommonWorkRepo; // Import renamed repo
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/common-work") // Changed base path to /api/common-work
public class CommonWorkController { // Renamed from WorkController

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private CommonWorkRepo commonWorkRepository; // Autowire renamed repo

    // Assign work to a specific user (existing method, keep it as is, or refine if needed)
    // The endpoint is now /api/common-work/users/{userId}/assign
    @PostMapping(value = "/users/{userId}/assign", consumes = "application/json")
    public ResponseEntity<String> assignWorkToUser(
        @PathVariable Long userId,
        @RequestBody CommonWorkEntity commonWork) { // Use renamed entity

        UserDetailsEntity user = userDetailsRepo.findById(userId)
            .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found");
        }

        // Link work to user
        commonWork.setUser(user);

        // Save the CommonWorkEntity
        commonWorkRepository.save(commonWork); // Use renamed repo

        // --- NEW LOGIC TO UPDATE UserDetailsEntity's 'work' column (if you still need it) ---
        // If you've decided to remove the 'String work' field from UserDetailsEntity,
        // then REMOVE this block below.
        String newWorkDescription = commonWork.getDescription();
        if (newWorkDescription != null && !newWorkDescription.isEmpty()) {
            String existingWorkInUserDetails = user.getWork();
            if (existingWorkInUserDetails == null || existingWorkInUserDetails.isEmpty()) {
                user.setWork(newWorkDescription);
            } else {
                user.setWork(existingWorkInUserDetails + ", " + newWorkDescription);
            }
            userDetailsRepo.save(user);
        }
        // --- END NEW LOGIC ---

        return ResponseEntity.ok("Work assigned successfully and user details updated");
    }

    /**
     * NEW: Endpoint for Admin to add general work (not tied to a specific user initially).
     * This work will be visible to all members.
     * The endpoint is now /api/common-work/add
     */
    @PostMapping("/add")
    public ResponseEntity<String> addGeneralWork(@RequestBody CommonWorkRequest commonWorkRequest) { // Use renamed DTO
        LocalDate dueDate;
        try {
            dueDate = (commonWorkRequest.getDueDate() != null && !commonWorkRequest.getDueDate().isEmpty()) ?
                      LocalDate.parse(commonWorkRequest.getDueDate()) : null;
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid due date format. Use YYYY-MM-DD.");
        }

        CommonWorkEntity newWork = new CommonWorkEntity(); // Use renamed entity
        newWork.setTitle(commonWorkRequest.getTitle());
        newWork.setDescription(commonWorkRequest.getDescription());
        newWork.setDueDate(dueDate);
        // Do NOT set a user here, as it's general work for all.

        commonWorkRepository.save(newWork); // Use renamed repo
        return ResponseEntity.status(HttpStatus.CREATED).body("Work added successfully!");
    }

    /**
     * NEW: Endpoint to retrieve all work for members to see.
     * The endpoint is now /api/common-work/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<CommonWorkEntity>> getAllWork() { // Return renamed entity list
        List<CommonWorkEntity> allWork = commonWorkRepository.findAll(); // Use renamed repo
        return ResponseEntity.ok(allWork);
    }
}

// --- DTO for CommonWorkRequest ---
class CommonWorkRequest { // Renamed from WorkRequest
    private String title;
    private String description;
    private String dueDate;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}