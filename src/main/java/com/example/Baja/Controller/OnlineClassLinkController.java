
package com.example.Baja.Controller;

import com.example.Baja.Entity.OnlineClassLinkEntity;
import com.example.Baja.Repo.OnlineClassLinkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/online-class")
public class OnlineClassLinkController {

    @Autowired
    private OnlineClassLinkRepo onlineClassLinkRepo;

    /**
     * DTO for incoming requests to add/update an online class link.
     */
    static class OnlineClassLinkRequest {
        private String meetLink;
        private String topic;

        public String getMeetLink() {
            return meetLink;
        }

        public void setMeetLink(String meetLink) {
            this.meetLink = meetLink;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }

    /**
     * Admin can add a new Google Meet link or update the existing one.
     * We'll manage a single "active" link by always creating/updating the first record.
     * If multiple are possible, this logic would need to change (e.g., add 'isActive' flag).
     */
    @PostMapping("/link")
    public ResponseEntity<String> addOrUpdateOnlineClassLink(@RequestBody OnlineClassLinkRequest request) {
        if (request.getMeetLink() == null || request.getMeetLink().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Meet link cannot be empty.");
        }

        List<OnlineClassLinkEntity> existingLinks = onlineClassLinkRepo.findAll();
        OnlineClassLinkEntity linkToSave;

        if (!existingLinks.isEmpty()) {
            // Update the first existing link
            linkToSave = existingLinks.get(0);
            linkToSave.setMeetLink(request.getMeetLink());
            linkToSave.setTopic(request.getTopic());
            linkToSave.setLastUpdated(LocalDateTime.now());
            onlineClassLinkRepo.save(linkToSave);
            return ResponseEntity.ok("Online class link updated successfully!");
        } else {
            // Create a new link
            linkToSave = new OnlineClassLinkEntity(request.getMeetLink(), request.getTopic());
            onlineClassLinkRepo.save(linkToSave);
            return ResponseEntity.status(HttpStatus.CREATED).body("Online class link added successfully!");
        }
    }

    /**
     * Get the current Google Meet link for members to see.
     */
    @GetMapping("/link")
    public ResponseEntity<OnlineClassLinkEntity> getCurrentOnlineClassLink() {
        // Return the first link found, or 204 No Content if none
        return onlineClassLinkRepo.findAll().stream()
                .findFirst()
                .map(link -> ResponseEntity.ok(link))
                .orElse(ResponseEntity.noContent().build()); // 204 No Content
    }

    /**
     * Admin can remove (clear) the current Google Meet link.
     */
    @DeleteMapping("/link")
    public ResponseEntity<String> removeOnlineClassLink() {
        List<OnlineClassLinkEntity> existingLinks = onlineClassLinkRepo.findAll();
        if (!existingLinks.isEmpty()) {
            onlineClassLinkRepo.deleteAll(existingLinks); // Deletes all (assuming only one active)
            return ResponseEntity.ok("Online class link removed successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No online class link found to remove.");
        }
    }
}