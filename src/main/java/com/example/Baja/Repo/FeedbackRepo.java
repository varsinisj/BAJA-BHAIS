package com.example.Baja.Repo; // Assuming your repository is in this package

import com.example.Baja.Entity.FeedbackEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import Query annotation
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepo extends JpaRepository<FeedbackEntity, Long> {
    // You can add custom query methods here if needed, e.g.,
     List<FeedbackEntity> findByUserId(Long userId);

    // New method to fetch all feedback along with their associated user details
    // using JOIN FETCH to prevent LazyInitializationException.
    @Query("SELECT f FROM FeedbackEntity f JOIN FETCH f.user")
    List<FeedbackEntity> findAllWithUserDetails();
}