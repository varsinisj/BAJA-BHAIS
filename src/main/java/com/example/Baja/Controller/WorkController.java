// WorkController.java
package com.example.Baja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Baja.Entity.UserDetailsEntity;
import com.example.Baja.Entity.WorkEntity; // Make sure this is your WorkEntity
import com.example.Baja.Repo.UserDetailsRepo;
import com.example.Baja.Repo.WorkRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api")
public class WorkController {

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private WorkRepo workRepository;

    // Assign work to a user using a structured JSON body (WorkEntity)
    @PostMapping(value = "/users/{userId}/work", consumes = "application/json")
    public ResponseEntity<String> assignWorkToUser(
        @PathVariable Long userId,
        @RequestBody WorkEntity work) {

        UserDetailsEntity user = userDetailsRepo.findById(userId)
            .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found");
        }

        // Link work to user (this establishes the relationship in the WorkEntity)
        work.setUser(user);

        // Save the WorkEntity to its own table
        workRepository.save(work);

        // --- NEW LOGIC TO UPDATE UserDetailsEntity's 'work' column ---
        // Get the description from the newly saved WorkEntity
        String newWorkDescription = work.getDescription(); // Assuming WorkEntity has a 'description' field for work
        if (newWorkDescription != null && !newWorkDescription.isEmpty()) {
            String existingWorkInUserDetails = user.getWork();
            if (existingWorkInUserDetails == null || existingWorkInUserDetails.isEmpty()) {
                user.setWork(newWorkDescription);
            } else {
                // Append the new work description to the existing one, comma-separated
                user.setWork(existingWorkInUserDetails + ", " + newWorkDescription);
            }
            // Save the updated UserDetailsEntity
            userDetailsRepo.save(user);
        }
        // --- END NEW LOGIC ---

        return ResponseEntity.ok("Work assigned successfully and user details updated");
    }
}