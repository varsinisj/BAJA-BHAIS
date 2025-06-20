package com.example.Baja.Service;

import com.example.Baja.Entity.FeedbackEntity;
import com.example.Baja.Entity.UserDetailsEntity;
import com.example.Baja.Repo.FeedbackRepo;
import com.example.Baja.Repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import this for @Transactional

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepo feedbackRepo;

    @Autowired
    private UserDetailsRepo userDetailsRepo; // To fetch the UserDetailsEntity

    @Transactional // Good practice to have this on write operations
    public FeedbackEntity saveFeedback(Long userId, String feedbackText) {
        // Find the UserDetailsEntity for the given userId
        Optional<UserDetailsEntity> userDetailsOptional = userDetailsRepo.findById(userId);

        if (userDetailsOptional.isPresent()) {
            UserDetailsEntity user = userDetailsOptional.get();
            // Use the constructor that takes UserDetailsEntity and feedbackText
            FeedbackEntity feedback = new FeedbackEntity(user, feedbackText);
            // The constructor already sets submissionDate = LocalDateTime.now()

            return feedbackRepo.save(feedback);
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
    }

    // --- MODIFICATION STARTS HERE ---
    @Transactional(readOnly = true) // Mark as read-only for optimized performance
    public List<FeedbackEntity> findAllFeedback() {
        // Use the new method from FeedbackRepo that eagerly fetches user details
        return feedbackRepo.findAllWithUserDetails();
    }
    // --- MODIFICATION ENDS HERE ---
}