package com.epam.ms.service.validation;

import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.service.exception.ConflictingDataException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator implements Validator{
    @NonNull
    private UserRepository repository;

    @Override
    public void validateOnCreate(User user) {
        checkEmailInUse(user.getEmail());
    }

    @Override
    public void validateOnUpdate(User user, String id) {
        checkEmailInUse(user.getEmail(), id);
    }

    private void checkEmailInUse(String email) {
        if(repository.findByEmail(email).isPresent()) {
            log.error("The user with the following email already exists: " + email);
            throw new ConflictingDataException("The user with the following email already exists: " + email);
        }
    }

    private void checkEmailInUse(String email, String id) {
        Optional<User> userWithSameEmail = repository.findByEmail(email);
        if(userWithSameEmail.isPresent() && !StringUtils.equals(userWithSameEmail.get().getId(), id)) {
            log.error("The user with the following email already exists: {}", email);
            throw new ConflictingDataException("The user with the following email already exists: " + email);
        }
    }
}
