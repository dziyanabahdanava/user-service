package com.epam.ms.service;

import com.epam.ms.event.Event;
import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.service.validation.UserValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_EVENTS_QUEUE = "userEventsQueue";
    private static final String GROUP = "user";
    private static final String USER_BLOCKED_EVENT = "user.blocked";
    private static final String USER_UNBLOCKED_EVENT = "user.unblocked";

    @NonNull
    private UserRepository repository;
    @NonNull
    private UserValidator validator;
    @NonNull
    private AmqpTemplate template;
    @NonNull
    private ObjectMapper jacksonMapper;

    public User create(User user) {
        validator.validateOnCreate(user);
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

    public User update(String id, User user) throws JsonProcessingException {
        Optional<User> existingUser = repository.findById(id);
        if(existingUser.isPresent()) {
            validator.validateOnUpdate(user, id);
            User currentUser = existingUser.get();
            checkUserStateAndNotify(user, currentUser);
            copyUserData(user, currentUser);
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

    private void checkUserStateAndNotify(User updatedUser, User existingUser) throws JsonProcessingException {
        if(updatedUser.isActive() && !existingUser.isActive()) {
            sendEventToQueue(existingUser.getId(), USER_UNBLOCKED_EVENT);
        } else if(!updatedUser.isActive() && existingUser.isActive()) {
            sendEventToQueue(existingUser.getId(), USER_BLOCKED_EVENT);
        }
    }

    private void sendEventToQueue(String id, String eventKey) throws JsonProcessingException {
        log.info("Send event {} to queue {}", eventKey, USER_EVENTS_QUEUE);
        Event event = new Event(GROUP, eventKey, ImmutableMap.of("id", id));
        template.convertAndSend(USER_EVENTS_QUEUE, jacksonMapper.writeValueAsString(event));
    }
}
