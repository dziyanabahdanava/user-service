package com.epam.ms.controller;

import com.epam.ms.repository.entity.User;
import com.epam.ms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {
    private static final String CREATED_USER_URI = "/users/%s";

    @Autowired
    private UserService service;

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        User user = service.getById(id);
        return isNull(user)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody User user) {
        User createdUser = service.create(user);
        return ResponseEntity.created(
                URI.create(String.format(CREATED_USER_URI, createdUser.getId())))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody User user) {
        Long createdUserId = service.update(id, user);
        return nonNull(createdUserId)
                ? ResponseEntity.created(URI.create(String.format(CREATED_USER_URI, createdUserId.toString()))).build()
                : ResponseEntity.noContent().build();

    }
}
