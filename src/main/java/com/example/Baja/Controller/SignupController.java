//package com.example.Baja.Controller;
//
//import com.example.Baja.Entity.SignupEntity;
//import com.example.Baja.Repo.SignupRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//public class SignupController {
//
//    @Autowired
//    private SignupRepo userRepository;
//
//    // DTO for register requests
//    public static class AuthRequest {
//        private String email;
//        private String password;
//
//        public String getEmail() { return email; }
//        public void setEmail(String email) { this.email = email; }
//        public String getPassword() { return password; }
//        public void setPassword(String password) { this.password = password; }
//    }
//
//    // ✅ Register a new user with plain text password (NOT recommended for production)
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
//        if (request.getEmail() == null || request.getPassword() == null ||
//                request.getEmail().isBlank() || request.getPassword().isBlank()) {
//            return ResponseEntity.badRequest().body(Map.of("message", "Email and password must not be empty"));
//        }
//
//        if (userRepository.findByEmail(request.getEmail()) != null) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User already exists"));
//        }
//
//        SignupEntity newUser = new SignupEntity();
//        newUser.setEmail(request.getEmail());
//        newUser.setPassword(request.getPassword()); // ✅ NO HASHING: store plain text
//
//        userRepository.save(newUser);
//        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
//    }
//
//    // ✅ Remove forgot/reset password with mail sending for now — you can add later if needed
//}
package com.example.Baja.Controller;

import com.example.Baja.Entity.SignupEntity;
import com.example.Baja.Repo.SignupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SignupController {

    @Autowired
    private SignupRepo userRepository;

    public static class AuthRequest {
        private String email;
        private String password;

        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (request.getEmail() == null || request.getPassword() == null ||
            request.getEmail().isBlank() || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email and password must not be empty"));
        }

        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "User already exists"));
        }

        SignupEntity newUser = new SignupEntity();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword()); // In production, hash passwords!

        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }
}
