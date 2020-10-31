package com.epam.ms.controller;

import com.epam.ms.repository.domain.UserProfile;
import com.epam.ms.service.UserProfileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * REST controller exposes an endpoint to work with user profiles
 * @author Dziyana Bahdanava
 */
@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserProfileController {
    @NonNull
    private UserProfileService profileService;

    @GetMapping("/{id}/profiles")
    public ResponseEntity<UserProfile> getUsersProfile(@PathVariable String id) {
        UserProfile userProfile = profileService.findByUserId(id);
        return isNull(userProfile)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(userProfile);
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfile>> getAll() {
        return ResponseEntity.ok(profileService.findAll());
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<UserProfile> getById(@PathVariable String id) {
        UserProfile userProfile = profileService.findById(id);
        return isNull(userProfile)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(userProfile);
    }

    @PostMapping("/profiles")
    public ResponseEntity<Void> create(@RequestBody UserProfile userProfile) {
        UserProfile createdUserProfile = profileService.create(userProfile);
        String id = createdUserProfile.getId();
        log.info("A new user profile is created: /users/profiles/{}", id);
        return ResponseEntity.created(
                URI.create(String.format("/user_profiles/%s", id)))
                .build();
    }

    @DeleteMapping("/profiles/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        profileService.delete(id);
        log.info("The user profile with id {} is deleted", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profiles/{id}")
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
