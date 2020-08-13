package com.epam.ms.service.validation;

import com.epam.ms.repository.IUserRepository;
import com.epam.ms.repository.entity.User;
import com.epam.ms.service.exception.EmailInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator {
    @Autowired
    private IUserRepository repository;

    public void checkEmailInUse(String email) {
        if(repository.findByEmail(email).isPresent()) {
            throw new EmailInUseException("The user with the following email already exists: " + email);
        }
    }

    public void checkEmailInUse(String email, Long id) {
        Optional<User> userWithSameEmail = repository.findByEmail(email);
        if(userWithSameEmail.isPresent() && !id.equals(userWithSameEmail.get().getId())) {
            throw new EmailInUseException("The user with the following email already exists: " + email);
        }
    }
}
