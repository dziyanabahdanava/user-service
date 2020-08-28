package com.epam.ms.service;

import com.epam.ms.queue.QueueHandler;
import com.epam.ms.repository.UserProfileRepository;
import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.repository.domain.UserProfile;
import com.epam.ms.service.validation.UserValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_BLOCKED_EVENT = "user.blocked";
    private static final String USER_UNBLOCKED_EVENT = "user.unblocked";

    @NonNull
    private UserRepository repository;
    @NonNull
    private UserProfileRepository userProfileRepository;
    @NonNull
    private UserValidator validator;
    @NonNull
    private QueueHandler queueHandler;

    public User create(User user) {
        validator.validateOnCreate(user);
        setUserProfile(user);
        return repository.save(user);
    }

    public List<User> findAll() {
        return (List<User>)repository.findAll();
    }

    public List<User> findByReceiveNotifications(Boolean receiveNotifications) {
        return repository.findByReceiveNotifications(receiveNotifications);
    }

    public User findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public User update(String id, User user) {
        Optional<User> existingUser = repository.findById(id);
        if(existingUser.isPresent()) {
            validator.validateOnUpdate(user, id);
            User currentUser = existingUser.get();
            checkUserStateAndNotify(user, currentUser);
            copyUserData(user, currentUser);
            setUserProfile(user);
            return repository.save(currentUser);
        } else {
            return null;
        }
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private User copyUserData(User from, User to) {
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setPassword(from.getPassword());
        to.setRole(from.getRole());
        to.setActive(from.isActive());

        return to;
    }

    private void checkUserStateAndNotify(User updatedUser, User existingUser) {
        if(updatedUser.isActive() && !existingUser.isActive()) {
            queueHandler.sendEventToQueue(existingUser.getId(), USER_UNBLOCKED_EVENT);
        } else if(!updatedUser.isActive() && existingUser.isActive()) {
            queueHandler.sendEventToQueue(existingUser.getId(), USER_BLOCKED_EVENT);
        }
    }

    private void setUserProfile(User user) {
        UserProfile userProfile = null;
        if(nonNull(user.getUserProfileId())){
            userProfile = userProfileRepository.findById(user.getUserProfileId()).orElse(null);
        }
        user.setUserProfile(userProfile);
    }
}
