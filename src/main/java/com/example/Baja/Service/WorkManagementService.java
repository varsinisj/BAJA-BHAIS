package com.example.Baja.Service;

import com.example.Baja.Entity.CommonWorkEntity;
import com.example.Baja.Entity.WorkEntity;
import com.example.Baja.Repo.CommonWorkRepo;
import com.example.Baja.Repo.WorkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import if you use transactional methods

import java.util.List;

@Service // Mark this class as a Spring Service component
public class WorkManagementService {

    @Autowired
    private WorkRepo workRepo; // For user-specific works

    @Autowired
    private CommonWorkRepo commonWorkRepo; // For common works

    /**
     * Retrieves all user-specific works for a given userId and all common works.
     * This method combines data from two different repositories.
     *
     * @param userId The ID of the logged-in user obtained from the session.
     * @return A CombinedWorkResponse containing lists of userWorks and commonWorks.
     */
    public CombinedWorkResponse getWorkForUser(Long userId) {
        // Fetch user-specific works using the ManyToOne relationship in WorkEntity
        // Your WorkEntity has: @ManyToOne @JoinColumn(name = "user_id") private UserDetailsEntity user;
        // So, the method in WorkRepo should be findByUser_Id(Long userId)
        List<WorkEntity> userWorks = workRepo.findByUser_Id(userId);

        // Fetch all common works (these are not user-specific)
        List<CommonWorkEntity> commonWorks = commonWorkRepo.findAll();

        // Return a DTO that holds both lists
        return new CombinedWorkResponse(userWorks, commonWorks);
    }

    // You can add other service methods here for admin functionalities, etc.,
    // which would be called by AdminController if you build one later.

    // Example of an admin method (not directly used by MemberController for 'See Work')
    @Transactional
    public WorkEntity saveIndividualWork(WorkEntity work) {
        // Add any business logic for saving individual work (e.g., validation)
        return workRepo.save(work);
    }
    
    @Transactional
    public CommonWorkEntity saveCommonWork(CommonWorkEntity commonWork) {
        // Add any business logic for saving common work
        return commonWorkRepo.save(commonWork);
    }
}