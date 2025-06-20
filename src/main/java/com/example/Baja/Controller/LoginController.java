package com.example.Baja.Controller;

import com.example.Baja.Entity.SignupEntity;
import com.example.Baja.Entity.UserDetailsEntity;
import com.example.Baja.Repo.SignupRepo;
import com.example.Baja.Repo.UserDetailsRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private SignupRepo signupRepo;

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials,
                                                     HttpSession session) {
        String email = credentials.get("email");
        String plainTextPassword = credentials.get("password");

        SignupEntity user = signupRepo.findByEmail(email);

        Map<String, String> response = new HashMap<>();

        if (user != null && user.getPassword().equals(plainTextPassword)) {

            Optional<UserDetailsEntity> userDetailsOptional = userDetailsRepo.findByEmail(email);

            if (userDetailsOptional.isPresent()) {
                UserDetailsEntity userDetails = userDetailsOptional.get();
                Long userId = userDetails.getId();
                String role = userDetails.getRole(); // Get role, but don't use it for initial redirect

                // Store info in session (important for RoleCheckController)
                session.setAttribute("email", email);
                session.setAttribute("role", role); // Store the actual role
                session.setAttribute("userId", userId);

                response.put("status", "success");
                response.put("message", "Login successful!");
                response.put("userId", String.valueOf(userId));
                response.put("role", role); // Still send role to frontend, useful for JS

                // --- CHANGE HERE: ALWAYS REDIRECT TO DASHBOARD.HTML FIRST ---
                response.put("redirectUrl", "/dashboard.html"); // All users go to dashboard initially

                return ResponseEntity.ok(response);

            } else {
                response.put("status", "error");
                response.put("message", "User details not found. Please complete your profile.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } else {
            response.put("status", "error");
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Logged out successfully!");
        return ResponseEntity.ok(response);
    }
}