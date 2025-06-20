package com.example.Baja.Service;

import com.example.Baja.Entity.CommonWorkEntity;
import com.example.Baja.Entity.WorkEntity;

import java.util.List;

public class CombinedWorkResponse {
    private List<WorkEntity> userWorks;
    private List<CommonWorkEntity> commonWorks;

    public CombinedWorkResponse(List<WorkEntity> userWorks, List<CommonWorkEntity> commonWorks) {
        this.userWorks = userWorks;
        this.commonWorks = commonWorks;
    }

    // Getters (needed for JSON serialization)
    public List<WorkEntity> getUserWorks() {
        return userWorks;
    }

    public List<CommonWorkEntity> getCommonWorks() {
        return commonWorks;
    }

    // Setters (optional, but good practice if you ever need to modify the lists after creation)
    public void setUserWorks(List<WorkEntity> userWorks) {
        this.userWorks = userWorks;
    }

    public void setCommonWorks(List<CommonWorkEntity> commonWorks) {
        this.commonWorks = commonWorks;
    }
}