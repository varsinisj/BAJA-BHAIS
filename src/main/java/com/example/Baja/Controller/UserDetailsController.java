package com.example.Baja.Controller;

import com.example.Baja.Entity.SignupEntity;
import com.example.Baja.Entity.UserDetailsEntity;
import com.example.Baja.Repo.SignupRepo;
import com.example.Baja.Repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserDetailsController {

    @Autowired
    private UserDetailsRepo detailsRepo;

    @Autowired
    private SignupRepo userRepository;

    /**
     * ✅ Save full details AND link them to the parent signup record.
     */
    @PostMapping("/details")
    public RedirectView submitDetails(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String rollNumber,
            @RequestParam String officialEmail,
            @RequestParam String department,
            @RequestParam String parentsName,
            @RequestParam String bloodGroup,
            @RequestParam String studentMobile,
            @RequestParam String parentMobile,
            @RequestParam String address,
            @RequestParam(required = false) String[] subsystems,
            @RequestParam String role,
            @RequestParam MultipartFile photo,
            @RequestParam MultipartFile idCardPhoto
    ) throws IOException {

        // ✅ 1) Lookup the parent signup record by email
        SignupEntity signup = userRepository.findByEmail(email);
        if (signup == null) {
            throw new RuntimeException("Signup not found for email: " + email);
        }

        // ✅ 2) Create & populate UserDetailsEntity
        UserDetailsEntity details = new UserDetailsEntity();
        details.setName(name);
        details.setEmail(email);
        details.setRollNumber(rollNumber);
        details.setOfficialEmail(officialEmail);
        details.setDepartment(department);
        details.setParentsName(parentsName);
        details.setBloodGroup(bloodGroup);
        details.setStudentMobile(studentMobile);
        details.setParentMobile(parentMobile);
        details.setAddress(address);
        if (subsystems != null) {
            details.setSubsystems(String.join(", ", subsystems));
        }
        details.setRole(role);
        details.setPhoto(photo.getBytes());
        details.setIdCardPhoto(idCardPhoto.getBytes());

        // ✅ 3) LINK the signup to the details
        details.setSignup(signup);

        // ✅ 4) Save details
        detailsRepo.save(details);

        return new RedirectView("/dashboard.html");
    }

    /**
     * ✅ List all user details
     */
    @GetMapping("/users")
    public List<UserDetailsEntity> getAllUsers() {
        return detailsRepo.findAll();
    }

    /**
     * ✅ Delete: removes both details & signup via cascade
     */
    @DeleteMapping("/users/{id}")
    public String deleteUserDetailsAndSignup(@PathVariable Long id) {
        Optional<UserDetailsEntity> optionalDetails = detailsRepo.findById(id);

        if (optionalDetails.isPresent()) {
            UserDetailsEntity details = optionalDetails.get();

            SignupEntity signup = details.getSignup();

            if (signup != null) {
                // ✅ Delete parent signup — cascade will remove details too
                userRepository.delete(signup);
            } else {
                // ✅ If somehow orphaned, just delete details
                detailsRepo.delete(details);
            }

            return "User details and linked signup deleted successfully";
        } else {
            return "User details not found";
        }
    }

    /**
     * ✅ Add work for a user
     */
    @PostMapping(value = "/users/{id}/work", consumes = "application/x-www-form-urlencoded")
    public String assignWork(@PathVariable Long id, @RequestParam String workDescription) {
        Optional<UserDetailsEntity> optionalUser = detailsRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return "User not found";
        }

        UserDetailsEntity user = optionalUser.get();
        String existingWork = user.getWork();
        if (existingWork == null || existingWork.isEmpty()) {
            user.setWork(workDescription);
        } else {
            user.setWork(existingWork + ", " + workDescription);
        }

        detailsRepo.save(user);
        return "Work assigned successfully to user ID: " + id;
    }
}
