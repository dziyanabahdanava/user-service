package com.epam.ms.service;

import com.epam.ms.repository.UserProfileRepository;
import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.repository.domain.UserProfile;
import com.epam.ms.service.validation.UserProfileValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {
    @NonNull
    private UserProfileRepository userProfileRepository;
    @NonNull
    private UserRepository userRepository;
    @NonNull
    private UserProfileValidator validator;

    public UserProfile create(UserProfile userProfile) {
        validator.validateOnCreate(userProfile);
        User user = userRepository.findById(userProfile.getUser().getId()).get();
        userProfile.setUser(user);
        return userProfileRepository.save(userProfile);
    }

    public List<UserProfile> findAll() {
        return (List<UserProfile>) userProfileRepository.findAll();
    }

    public UserProfile findByUserId(String userId) {
        return userProfileRepository.findByUserId(userId).orElse(null);
    }

    public UserProfile findById(String id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    public UserProfile update(String id, UserProfile userProfile) {
        Optional<UserProfile> existingProfile = userProfileRepository.findById(id);
        if(existingProfile.isPresent()) {
            validator.validateOnUpdate(userProfile, id);
            UserProfile currentUserProfile = existingProfile.get();
            copyUserProfileData(userProfile, currentUserProfile);
            User user = userRepository.findById(userProfile.getUser().getId()).get();
            userProfile.setUser(user);
            return userProfileRepository.save(currentUserProfile);
        } else {
            return null;
        }
    }

    public void delete(String id) {
        userProfileRepository.deleteById(id);
    }

    private void copyUserProfileData(UserProfile from, UserProfile to) {
        to.setAge(from.getAge());
        to.setHeight(from.getHeight());
        to.setWeight(from.getWeight());
        to.setGoal(from.getGoal());
        to.setPhysicalActivity(from.getPhysicalActivity());
    }
}
