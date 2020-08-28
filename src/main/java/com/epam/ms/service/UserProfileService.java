package com.epam.ms.service;

import com.epam.ms.repository.UserProfileRepository;
import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.repository.domain.UserProfile;
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

    public UserProfile create(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public List<UserProfile> findAll() {
        return (List<UserProfile>) userProfileRepository.findAll();
    }

    public UserProfile findByUserId(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.flatMap(value -> userProfileRepository.findById(value.getUserProfile().getId()))
                .orElse(null);
    }

    public UserProfile findById(String id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    public UserProfile update(String id, UserProfile userProfile) {
        Optional<UserProfile> existingUser = userProfileRepository.findById(id);
        if(existingUser.isPresent()) {
            UserProfile currentUserProfile = existingUser.get();
            copyUserProfileData(userProfile, currentUserProfile);
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
