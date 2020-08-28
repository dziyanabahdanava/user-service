package com.epam.ms.controller;

import com.epam.ms.repository.domain.UserProfile;
import com.epam.ms.service.UserProfileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * REST controller exposes an endpoint to work with user profiles
 * @author Dziyana Bahdanava
 */
@RestController
@RequestMapping("/user_profiles")
@Slf4j
@RequiredArgsConstructor
public class UserProfileController {
    @NonNull
    private UserProfileService profileService;

    @GetMapping
    public ResponseEntity<List<UserProfile>> getAll(@RequestParam (required = false) String userId) {
        return isNull(userId) ?
                ResponseEntity.ok(profileService.findAll())
                : ResponseEntity.ok(singletonList(profileService.findByUserId(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getById(@PathVariable String id) {
        UserProfile userProfile = profileService.findById(id);
        return isNull(userProfile)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(userProfile);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserProfile userProfile) {
        UserProfile createdUserProfile = profileService.create(userProfile);
        String id = createdUserProfile.getId();
        log.info("A new user profile is created: /user_profiles/{}", id);
        return ResponseEntity.created(
                URI.create(String.format("/user_profiles/%s", id)))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        profileService.delete(id);
        log.info("The user profile with id {} is deleted", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody UserProfile userProfile) {
        UserProfile createdUserProfile = profileService.update(id, userProfile);
        if(nonNull(createdUserProfile)) {
            log.info("The user profile with id {} is updated", id);
            return ResponseEntity.noContent().build();
        } else {
            log.error("The user profile with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
