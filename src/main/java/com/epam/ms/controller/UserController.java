package com.epam.ms.controller;

import com.epam.ms.repository.domain.User;
import com.epam.ms.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * REST controller exposes an endpoint to work with users
 * @author Dziyana Bahdanava
 */
@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    @NonNull
    private UserService service;

    @NonNull
    AmqpTemplate template;

    @GetMapping
    public ResponseEntity<List<User>> getAll(@RequestParam(required = false) Boolean receiveNotifications) {
        return isNull(receiveNotifications)
                ? ResponseEntity.ok(service.findAll())
                : ResponseEntity.ok(service.findByReceiveNotifications(receiveNotifications));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        User user = service.findById(id);
        return isNull(user)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody User user) {
        User createdUser = service.create(user);
        String id = createdUser.getId();
        log.info("A new user is created: /users/{}", id);
        return ResponseEntity.created(
                URI.create(String.format("/users/%s", id)))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        log.info("The user with id {} is deleted", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody User user) throws JsonProcessingException {
        User createdUser = service.update(id, user);
        if(nonNull(createdUser)) {
            log.info("The user with id {} is updated", id);
            return ResponseEntity.noContent().build();
        } else {
            log.error("The user with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
