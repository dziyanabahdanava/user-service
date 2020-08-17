package com.epam.ms.service;

import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.service.validation.UserValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @NonNull
    private UserRepository repository;

    @NonNull
    private UserValidator validator;

    public User create(User user) {
        validator.checkEmailInUse(user.getEmail());
        return repository.save(user);
    }

    public List<User> findAll() {
        return (List)repository.findAll();
    }

    public User findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public User update(String id, User user) {
        Optional<User> existingUser = repository.findById(id);
        if(existingUser.isPresent()) {
            validator.checkEmailInUse(user.getEmail(), id);
            User currentUser = existingUser.get();
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

        return to;
    }
}
