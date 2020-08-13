package com.epam.ms.service;

import com.epam.ms.repository.IUserRepository;
import com.epam.ms.repository.entity.User;
import com.epam.ms.service.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private IUserRepository repository;

    @Autowired
    private UserValidator validator;

    public User create(User user) {
        validator.checkEmailInUse(user.getEmail());
        return repository.save(user);
    }

    public List<User> getAll() {
        return (List)repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Long update(Long id, User user) {
        Optional<User> existingUser = repository.findById(id);
        if(!existingUser.isPresent()) {
            validator.checkEmailInUse(user.getEmail());
            return repository.save(user).getId();
        } else {
            validator.checkEmailInUse(user.getEmail(), id);
            User currentUser = existingUser.get();
            copyUserData(user, currentUser);
            repository.save(currentUser);
            return null;
        }
    }

    public void delete(Long id) {
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
