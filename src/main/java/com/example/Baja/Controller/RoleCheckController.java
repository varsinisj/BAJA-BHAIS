package com.example.Baja.Controller;

import com.example.Baja.Entity.UserDetailsEntity;
import com.example.Baja.Repo.UserDetailsRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional; // <--- MAKE SURE THIS IMPORT IS PRESENT

@RestController
@RequestMapping("/api")
public class RoleCheckController {

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @GetMapping("/check-role")
    public ResponseEntity<Map<String, String>> checkUserRole(HttpSession session) {
        // Retrieve email from session (must be set in LoginController)
        String email = (String) session.getAttribute("email");

        Map<String, String> response = new HashMap<>();

        if (email == null) {
            response.put("role", "unknown");
            response.put("message", "User email not found in session.");
            return ResponseEntity.status(401).body(response); // 401 Unauthorized
        }

        // --- FIX FOR THE TYPE MISMATCH ERROR HERE ---
        Optional<UserDetailsEntity> userOptional = userDetailsRepo.findByEmail(email);

        if (userOptional.isPresent()) {
            UserDetailsEntity userDetails = userOptional.get(); // Get the actual UserDetailsEntity
            String role = userDetails.getRole().toLowerCase();
            response.put("role", role);
            response.put("message", "Role retrieved successfully.");
            return ResponseEntity.ok(response); // 200 OK
        } else {
            response.put("role", "unknown");
            response.put("message", "User details not found for the provided email.");
            return ResponseEntity.status(404).body(response); // 404 Not Found, as email exists but details don't
        }
    }
}