package com.epam.ms.service.validation;

import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.service.exception.EmailInUseException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {
    @NonNull
    private UserRepository repository;

    public void checkEmailInUse(String email) {
        if(repository.findByEmail(email).isPresent()) {
            log.error("The user with the following email already exists: " + email);
            throw new EmailInUseException("The user with the following email already exists: " + email);
        }
    }

    public void checkEmailInUse(String email, String id) {
        Optional<User> userWithSameEmail = repository.findByEmail(email);
        if(userWithSameEmail.isPresent() && !id.equals(userWithSameEmail.get().getId())) {
            log.error("The user with the following email already exists: " + email);
            throw new EmailInUseException("The user with the following email already exists: " + email);
        }
    }
}
