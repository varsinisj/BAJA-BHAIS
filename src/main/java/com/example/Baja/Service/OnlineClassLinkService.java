package com.example.Baja.Service;

import com.example.Baja.Entity.OnlineClassLinkEntity;
import com.example.Baja.Repo.OnlineClassLinkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OnlineClassLinkService {

    @Autowired
    private OnlineClassLinkRepo onlineClassLinkRepo;

    /**
     * Retrieves the current or most recent online class link.
     * You might need to refine this logic based on how you determine the "current" link.
     * For now, it gets the one with the highest ID (assumed to be the most recent).
     * @return An Optional containing the OnlineClassLinkEntity if found, empty otherwise.
     */
    public Optional<OnlineClassLinkEntity> getCurrentClassLink() {
        return onlineClassLinkRepo.findTopByOrderByIdDesc();
    }
}