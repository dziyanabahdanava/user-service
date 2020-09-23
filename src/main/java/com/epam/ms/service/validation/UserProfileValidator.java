package com.epam.ms.service.validation;

import com.epam.ms.repository.UserProfileRepository;
import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.repository.domain.UserProfile;
import com.epam.ms.service.exception.ConflictingDataException;
import com.epam.ms.service.exception.ResourceException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileValidator implements Validator<UserProfile> {
    @NonNull
    private UserRepository userRepository;

    @NonNull
    private UserProfileRepository userProfileRepository;

    @Override
    public void validateOnCreate(UserProfile profile) {
        checkUserExists(profile.getUser().getId());
        checkProfileAlreadyExists(profile.getUser().getId());
    }

    @Override
    public void validateOnUpdate(UserProfile profile, String id) {
        checkUserExists(profile.getUser().getId());
    }

    private void checkUserExists(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new ResourceException("The user with the following id does not exist: " + userId);
        }
    }

    private void checkProfileAlreadyExists(String userId) {
        Optional<UserProfile> userProfile = userProfileRepository.findByUserId(userId);
        if(userProfile.isPresent()) {
            throw new ConflictingDataException("The user " + userId + " already has a profile");
        }
    }

}
